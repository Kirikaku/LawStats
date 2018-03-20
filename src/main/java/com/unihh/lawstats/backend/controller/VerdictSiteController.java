package com.unihh.lawstats.backend.controller;

import com.unihh.lawstats.backend.repository.VerdictRepoService;
import com.unihh.lawstats.core.mapping.BGHVerdictUtil;
import com.unihh.lawstats.core.mapping.VerdictDateFormatter;
import com.unihh.lawstats.core.model.attributes.DataModelAttributes;
import com.unihh.lawstats.core.model.Verdict;
import com.unihh.lawstats.core.model.input.Input;
import com.unihh.lawstats.core.model.input.StringInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
@Service("VerdictSiteController")
public class VerdictSiteController {

    @Autowired
    private VerdictRepoService verdictRepoService;

    private Verdict verdict;

    /**
     * This method is related to url: /verdict/{docketNumber}/**
     */
    @RequestMapping(value = "/verdict/{givenDocketNumber}/**")
    public String getVerdictSite(Model model, @PathVariable String givenDocketNumber, HttpServletRequest request) {
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();

        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);

        String docketNumber;
        if (null != arguments && !arguments.isEmpty()) {
           docketNumber = givenDocketNumber + '/' + arguments;
        } else {
           docketNumber = givenDocketNumber;
        }

        Set<Input> docketValueSet = new HashSet<>();
        StringInput stringInput = new StringInput();
        stringInput.setAttribute(DataModelAttributes.DocketNumber);
        stringInput.setValue(docketNumber);
        docketValueSet.add(stringInput);
        Collection<? extends Verdict> verdictList = verdictRepoService.getVerdictsForAttribute(DataModelAttributes.DocketNumber, docketValueSet);

        if (!verdictList.isEmpty()) {
            verdict = verdictList.iterator().next();
            model.addAttribute("verdict", verdict);
        } else {
            return "/";
        }

        return "verdictSite";
    }

    /**
     * This array returns a String with appended values of the given array
     */
    public String getStringFromArray(String[] judgeArray){
        if(judgeArray != null) {
            StringBuilder sb = new StringBuilder();
            Arrays.stream(judgeArray).forEach(s -> sb.append(s).append(", "));
            sb.delete(sb.length() - 1, sb.length());
            return sb.toString();
        }
        return "";
    }


    /**
     * Returns a Text indicating the revision outcome based on the standardized int value.
     * @param revisionSucces standardized int value indictaing the revision outcome
     * @return Text indicating the revision outcome
     */
    public String getRevisionSuccessTextForInt(int revisionSucces){
        String revisionSuccessText = "UNKOWN";

        switch(revisionSucces){
            case -1: revisionSuccessText = "Revision nicht erfolgreich";
                    break;
            case 0: revisionSuccessText = "Revision teilweise erfolgreich";
                    break;
            case 1: revisionSuccessText = "Revision erfolgreich";
                    break;
            default: revisionSuccessText = "UNKOWN";
                    break;
        }


       return revisionSuccessText;
    }

    /**
     * This method formats the given long and returns a strng
     */
    public String getStringFromDateLong(Long dateLong){
        if(dateLong != null) {
            VerdictDateFormatter verdictDateFormatter = new VerdictDateFormatter();
            return verdictDateFormatter.formatVerdictDateToString(dateLong);
        }
        return "";
    }

    /**
     * this method formats the given documentId into an url of the BGH
     */
    public String getBGHUrlWhenAvailable(int documentIdInBGH){
        BGHVerdictUtil bghVerdictUtil = new BGHVerdictUtil();
        return bghVerdictUtil.retrieveBGHVerdictUrl(documentIdInBGH);
    }
}
