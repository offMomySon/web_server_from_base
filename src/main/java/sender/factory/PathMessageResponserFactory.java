package sender.factory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sender.strategy.DirectoryMessageResponser;
import sender.strategy.FileMessageResponser;
import sender.strategy.MessageResponser;
import sender.strategy.NotFoundMessageResponser;

public class PathMessageResponserFactory implements AbstractMessageResponserFactory{
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public boolean isSupported(String path) {
    return true;
  }

  //메서드 단위 주석
  @Override
  public MessageResponser createMessageResponser(String path){
    logger.info("Check resource status by requestTarget[{}]", path);

    File file = new File(path);

    if(!file.exists()){
      return new NotFoundMessageResponser();
    }

    if(file.isFile()){
      return new FileMessageResponser(path);
    }

    if(file.isDirectory()){
      return new DirectoryMessageResponser(path);
    }





//    File file = new File(getFilePath(requestTarget));
//
//    if (file.exists()) {
//      if (file.isDirectory()) {
//        logger.info("File Status : directory exist");
//        return ResourceStatus.DIRECTORY_EXIST;
//      } else if (file.isFile()) {
//        logger.info("File Status : file exist");
//        return ResourceStatus.FILE_EXIST;
//      }
//    }
//
//    logger.info("File not exist at filePath[{}]", filePath);
//    return ResourceStatus.PATH_NOT_EXIST;
    return null;
  }
}
