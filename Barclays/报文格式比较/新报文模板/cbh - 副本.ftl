<?xml version="1.0" encoding="utf‐8"?>
<CHTR>
	<RBIF>
		<RICD>${RBIF.RICD}</RICD>
		<TSTN>${RBIF.CTTN}</TSTN>
		<MIRS>${RBIF.MIRS}</MIRS><!-- 新增 -->
	</RBIF>
	<TSDTs>
		<#list TSDTs as TSDT>
		<TSDT seqno="${TSDT.SEQNO}">
			<OCNM>${TSDT.OCNM}</OCNM>
			<OTDT>${TSDT.OTDT}</OTDT>
			<OTCD>${TSDT.OTCD}</OTCD>
			<OTIC>${TSDT.OTIC}</OTIC>
			<RINI>
				<FINC>${TSDT.FINC}</FINC>
				<RLFC>${TSDT.RLFC}</RLFC><!-- 新增 -->
			</RINI>
			<ATIF>
				<CATP>${TSDT.CATP}</CATP>
				<CTAC>${TSDT.CTAC}</CTAC>
				<OATM>${TSDT.OATM}</OATM><!-- 新增 -->
				<CBCT>${TSDT.CBCT}</CBCT><!-- 新增 -->
				<OCBT>${TSDT.OCBT}</OCBT><!-- 新增 -->
				<CBCN>${TSDT.CBCN}</CBCN><!-- 新增 -->
			</ATIF>
			<TBIF>
				<TBNM>${TSDT.TBNM}</TBNM>
				<TBIT>${TSDT.TBIT}</TBIT>
				<OITP>${TSDT.OITP}</OITP><!-- 新增，下有重复 -->
				<TBID>${TSDT.TBID}</TBID>
				<TBNT>${TSDT.TBNT}</TBNT>
			</TBIF>
			<TSIF>
				<TSTM>${TSDT.TSTM}</TSTM>
				<TRCD>${TSDT.TRCD}</TRCD>
				<TICD>${TSDT.TICD}</TICD>
				<RPMT>${TSDT.RPMT}</RPMT><!-- 新增 -->
				<RPMN>${TSDT.RPMN}</RPMN><!-- 新增 -->
				<TSTP>${TSDT.TSTP}</TSTP>
				<OCTT>${TSDT.OCTT}</OCTT><!-- 新增 -->
				<OOCT>${TSDT.OOCT}</OOCT><!-- 新增 -->
				<OCEC>${TSDT.OCEC}</OCEC><!-- 新增 -->
				<BPTC>${TSDT.BPTC}</BPTC><!-- 新增 -->
				<TSCT>${TSDT.TSCT}</TSCT>
				<TSDR>${TSDT.TSDR}</TSDR>
				<CRPP>${TSDT.CRPP}</CRPP>
				<CRTP>${TSDT.CRTP}</CRTP>
				<CRAT>${TSDT.CRAT}</CRAT>
				<CRMB>${TSDT.CRMB}</CRMB><!-- 新增 -->
				<CUSD>${TSDT.CUSD}</CUSD><!-- 新增 -->
			</TSIF>
			<TCIF>
				<CFIN>${TSDT.CFIN}</CFIN>
				<CFCT>${TSDT.CFCT}</CFCT>
				<CFIC>${TSDT.CFIC}</CFIC>
				<CFRC>${TSDT.CFRC}</CFRC><!-- 新增 -->
				<TCNM>${TSDT.TCNM}</TCNM>
				<TCIT>${TSDT.TCIT}</TCIT>
				<OITP>${TSDT.OITP}</OITP><!-- 新增，重复1 -->
				<TCID>${TSDT.TCID}</TCID>
				<TCAT>${TSDT.TCAT}</TCAT>
				<TCAC>${TSDT.TCAC}</TCAC>
			</TCIF>
			<ROTFs>
				<#list CommonFilter.doFilter(TSDT, ROTFs, "P_SEQNO1=P_SEQNO1") as ROTF>
				<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF}</ROTF><!-- 新增 -->
				<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF}</ROTF><!-- 新增 -->
			</ROTFs>
		</TSDT>
		</#list>
	</TSDTs>
</CHTR>