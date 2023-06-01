package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.TimeMap;

import java.io.FileNotFoundException;

public interface TimeMapService {

    TimeMap getTimeMap(String mapKey) throws FrontendModelException, FileNotFoundException;
}
