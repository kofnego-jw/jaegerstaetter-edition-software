package at.kulinz.jaegerstaetter.formaldesc.service;

import at.kulinz.jaegerstaetter.frontendmodel.dtoobj.StatReport;

import java.util.List;

public interface FormalDescService {

    StatReport createEditionStats(List<String> fileIds) throws Exception;

}
