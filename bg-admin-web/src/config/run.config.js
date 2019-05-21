import {runDevConfig} from './dev';
import {runProdConfig} from './prod';
import {runMockConfig} from './mock';
import env from './env';

export default {
  /**
   * 根据启动项env来获取配置文件
   */
  runConfig: env === 'development' ? runDevConfig : (env == 'mock' ? runMockConfig : runProdConfig),
}
