package com.musinsa.sys.point.controller;

import com.musinsa.sys.common.dto.ProcessResult;
import com.musinsa.sys.common.enums.ProcessCode;
import com.musinsa.sys.point.dto.*;
import com.musinsa.sys.point.service.PointService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("point")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saving/approval", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProcessResult<PointResp> pointSavingApproval(@Valid @RequestBody PointSavingApprovalReq pointSavingApprovalReq) {
        PointResp pointResp = pointService.savingApproval(pointSavingApprovalReq);
        return new ProcessResult<>(pointResp, ProcessCode.HCO000.getProcCd());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saving/cancel", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProcessResult<PointResp> pointSavingApproval(@Valid @RequestBody PointSavingCancelReq pointSavingCancelReq) {

        PointResp pointResp = pointService.savingCancel(pointSavingCancelReq);
        return new ProcessResult<>(pointResp, ProcessCode.HCO000.getProcCd());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/use/approval", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProcessResult<PointUseApprovalResp> pointUseApproval(@Valid @RequestBody PointUseApprovalReq pointUseApprovalReq) {

        PointUseApprovalResp pointUseApprovalResp = pointService.useApproval(pointUseApprovalReq);

        return new ProcessResult<>(pointUseApprovalResp, ProcessCode.HCO000.getProcCd());
    }
    @RequestMapping(method = RequestMethod.POST, value = "/use/cancel", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ProcessResult<PointResp> pointUseCancel(@Valid @RequestBody PointUseCancelReq pointUseCancelReq) {

        PointResp pointResp = pointService.useCancel(pointUseCancelReq);

        return new ProcessResult<>(pointResp, ProcessCode.HCO000.getProcCd());
    }

/*    @RequestMapping(method = RequestMethod.POST, value = "/expire")
    public ProcessResult<PointExpireResp> pointExpire(HttpServletRequest request, @Valid @RequestBody PointExpireReq pointExpireReq) throws Exception {

        PointExpireResp pointExpireResp = pointService.pointExpire(pointExpireReq);

        return ResultProcess.convertTo(pointExpireResp);
    }*/

}
