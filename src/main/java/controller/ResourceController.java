package controller;

import status.ResourceStatus;

import java.io.File;

public class ResourceController {

    public ResourceStatus getResourceStatus(String filePath) {
        File file = new File(filePath);

        if(file.exists()){
            System.out.println("file exist");
            if(file.isDirectory()){
                System.out.println("this is directory.");
                return ResourceStatus.DIRECTORY_EXIST;
            }else if(file.isFile()){
                System.out.println("this is file.");
                return ResourceStatus.FILE_EXIST;
            }
        }

        System.out.println("path not exist");
        return ResourceStatus.PATH_NOT_EXIST;
    }
}
