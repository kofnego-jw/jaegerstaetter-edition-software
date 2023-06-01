package at.kulinz.jaegerstaetter.frontendmodel.serviceapi;

import at.kulinz.jaegerstaetter.frontendmodel.FrontendModelException;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.Biography;
import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.BiographyFW;

import java.io.FileNotFoundException;
import java.util.List;

public interface BiographyService {

    List<BiographyFW> listBiographies();

    Biography getBiography(String filename) throws FrontendModelException, FileNotFoundException;

    byte[] getImage(String filename) throws FileNotFoundException, FrontendModelException;

    byte[] getPdf(String filename) throws FileNotFoundException, FrontendModelException;
}
