import {runDevConfig} from './dev';
import {runProdConfig} from './prod';
import env from './env';

export default {
  /**
   * 根据启动项env来获取配置文件
   */
  runConfig:env === 'development' ? runDevConfig : runProdConfig,
}
