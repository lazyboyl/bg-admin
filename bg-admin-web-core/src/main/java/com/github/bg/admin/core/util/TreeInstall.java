package com.github.bg.admin.core.util;



import com.github.bg.admin.core.dto.TreeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author linzf
 * @since 2019/5/5
 * 类描述：实现组装菜单树的数据
 */
public class TreeInstall {


    /**
     * 功能描述：实现递归组装选中的菜单节点的数据
     * @param treeList 全部菜单节点的集合
     * @param treeMap
     * @return
     */
    public static List<TreeDto> installCheckTree(List<TreeDto> treeList, Map<String,Object> treeMap){
        List<TreeDto> trees = new ArrayList<>();
        for(TreeDto roleTree : treeList ){
            if(0 == roleTree.getParentTreeId()){
                roleTree.setChildren(getCheckChild(roleTree.getTreeId(),treeList,treeMap));
                // 用于防止子节点部分选中，而父节点全部选中
                if(roleTree.getChildren().size()>0){
                    for(TreeDto checkTree:roleTree.getChildren()){
                        if(!checkTree.isChecked()){
                            roleTree.setChecked(false);
                            break;
                        }
                    }
                }
                trees.add(roleTree);
            }
        }
        return trees;
    }

    /**
     * 功能描述：实现树形菜单数据的组装
     * @param treeList
     */
    public static  List<TreeDto> installTree(List<TreeDto> treeList){
        List<TreeDto> trees = new ArrayList<>();
        for(TreeDto roleTree : treeList ){
            if(0 == roleTree.getParentTreeId()){
                roleTree.setChildren(getChild(roleTree.getTreeId(),treeList));
                trees.add(roleTree);
            }
        }
        return trees;
    }


    /**
     * 功能描述：递归遍历菜单节点
     * @param id
     * @param trees
     * @param treeMap
     * @return
     */
    private static List<TreeDto> getCheckChild(Integer id, List<TreeDto> trees, Map<String,Object> treeMap) {
        List<TreeDto> childList = new ArrayList<>();
        for (TreeDto tree : trees) {
            if(tree.getParentTreeId().intValue()==id.intValue()){
                tree.setChildren(getCheckChild(tree.getTreeId(),trees,treeMap));
                if(treeMap.get(tree.getTreeId().toString())!=null){
                    tree.setChecked(true);
                }else{
                    tree.setChecked(false);
                }
                if(tree.getChildren().size()>0){
                    for(TreeDto checkTree:tree.getChildren()){
                        if(!checkTree.isChecked()){
                            tree.setChecked(false);
                            break;
                        }
                    }
                }
                childList.add(tree);
            }
        }
        return childList;
    }

    /**
     * 功能描述：递归遍历菜单节点
     * @param id
     * @param trees
     * @return
     */
    private static List<TreeDto> getChild(Integer id, List<TreeDto> trees) {
        List<TreeDto> childList = new ArrayList<>();
        for (TreeDto tree : trees) {
            if(tree.getParentTreeId().intValue()==id.intValue()){
                tree.setChildren(getChild(tree.getTreeId(),trees));
                childList.add(tree);
            }
        }
        return childList;
    }

}
