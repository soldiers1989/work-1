<?xml version="1.0" encoding="gb18030"?>
<HVTR>
  <RBIF> 
    <RINM>${RBIF.RINM!}</RINM>
    <RICD>${RBIF.RICD!}</RICD>
    <RPDT>${RBIF.RPDT!}</RPDT>
    <CTTN>${RBIF.CTTN!}</CTTN>
  </RBIF>
  <CATIs>
    <#list CATIs as CATI>
    <CATI seqno="${CATI.SEQNO!}">
      <CTIF>
        <CTNM>${CATI.CTNM!}</CTNM>
        <CITP>${CATI.CITP!}</CITP>
        <CTID>${CATI.CTID!}</CTID>
        <CSNM>${CATI.CSNM!}</CSNM>
        <CTNT>${CATI.CTNT!}</CTNT>
      </CTIF>
      <HTDT>${CATI.HTDT!}</HTDT>
      <HTCRs>
        <#list CommonFilter.doFilter(CATI, HTCRs, "P_SEQNO1=P_SEQNO1") as HTCR>
        <HTCR seqno="${HTCR_index+1!}">
          <CRCD>${HTCR.CRCD!}</CRCD>
          <TTNM>${HTCR.TTNM!}</TTNM>
          <TSDTs>
            <#list CommonFilter.doFilter(HTCR, TSDTs, "P_SEQNO2=P_SEQNO2","P_SEQNO1=P_SEQNO1") as TSDT>
            <TSDT seqno="${TSDT_index+1!}">
              <RINI>
                <FINN>${TSDT.FINN!}</FINN>
                <FIRC>${TSDT.FIRC!}</FIRC>
                <RLTP>${TSDT.RLTP!}</RLTP>
                <FICT>${TSDT.FICT!}</FICT>
                <FINC>${TSDT.FINC!}</FINC>
                <CATP>${TSDT.CATP!}</CATP>
                <CTAC>${TSDT.CTAC!}</CTAC>
              </RINI>      
              <TBIF>
                <TBNM>${TSDT.TBNM!}</TBNM>
                <TBIT>${TSDT.TBIT!}</TBIT>
                <TBID>${TSDT.TBID!}</TBID>
                <TBNT>${TSDT.TBNT!}</TBNT>
              </TBIF>
              <TSIF>
                <TSTM>${TSDT.TSTM!}</TSTM>
                <TICD>${TSDT.TICD!}</TICD>
                <TSTP>${TSDT.TSTP!}</TSTP>
                <TSCT>${TSDT.TSCT!}</TSCT>
                <TSDR>${TSDT.TSDR!}</TSDR>
                <TDRC>${TSDT.TDRC!}</TDRC>
                <TRCD>${TSDT.TRCD!}</TRCD>
                <CRPP>${TSDT.CRPP!}</CRPP>
                <CRTP>${TSDT.CRTP!}</CRTP>
                <CRAT>${TSDT.CRAT!}</CRAT>
              </TSIF>
              <TCIF>
                <CFIN>${TSDT.CFIN!}</CFIN>
                <CFCT>${TSDT.CFCT!}</CFCT>
                <CFIC>${TSDT.CFIC!}</CFIC>
                <TCNM>${TSDT.TCNM!}</TCNM>
                <TCIT>${TSDT.TCIT!}</TCIT>
                <TCID>${TSDT.TCID!}</TCID>
                <TCAT>${TSDT.TCAT!}</TCAT>
                <TCAC>${TSDT.TCAC!}</TCAC>
              </TCIF>
            </TSDT>
          </#list>
          </TSDTs>
        </HTCR>
        </#list>
      </HTCRs>
    </CATI>
    </#list>
  </CATIs>
</HVTR>