package com.musinsa.sys.common.dto;

import com.musinsa.sys.common.enums.ProcessCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class ProcessResult<D> {
    private String sucsFalr;        // 성공실패여부
    private String rsltCd;            // 결과코드
    private String rsltMesg;        // 결과메시지
    private D rsltObj;                // 결과객체

    public ProcessResult(D rsltObj, String inProcCd) {
        ProcessCode processCode = ProcessCode.findByProcessCode(inProcCd);
        this.sucsFalr = processCode.getSucsFalr();
        this.rsltCd = processCode.getProcCd();
        this.rsltMesg = processCode.getRsltMesg();
        this.rsltObj = rsltObj;
    }


}
