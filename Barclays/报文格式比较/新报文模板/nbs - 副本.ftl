<?xml version="1.0" encoding="utf‐8"?>
<BSTR>
	<RBIF>
		<RICD>${RBIF.RICD!}</RICD><!-- 新增 -->
		<RPNC>${RBIF.RPNC!}</RPNC><!-- 新增 -->
		<DETR>${RBIF.DETR!}</DETR><!-- 新增 -->
		<TORP>${RBIF.TORP!}</TORP><!-- 新增 -->
		<ORXN>${RBIF.ORXN!}</ORXN><!-- 新增 -->
		<DORP>${RBIF.DORP!}</DORP><!-- 新增 -->
		<ODRP>${RBIF.ODRP!}</ODRP><!-- 新增 -->
		<TPTR>${RBIF.TPTR!}</TPTR><!-- 新增 -->
		<OTPR>${RBIF.OTPR!}</OTPR><!-- 新增 -->
		<STCB>${RBIF.STCB!}</STCB><!-- 新增 -->
		<AOSP>${RBIF.AOSP!}</AOSP><!-- 新增 -->
		<TOSCs>
			<#list TOSCs as TOSC>
			<TOSC seqno="${TOSC.SEQNO!}">${TOSC.TOSC!}</TOSC><!-- 新增 -->
			</#list>
		</TOSCs>
		<STCRs>
			<#list STCRs as STCR>
			<STCR seqno="${STCR.SEQNO!}">${STCR.STCR!}</STCR><!-- 新增 -->
			</#list>
		</STCRs>
		<SETN>${RBIF.SETN!}</SETN><!-- 新增 -->
		<STNM>${RBIF.STNM!}</STNM><!-- 新增 -->
		<RPNM>${RBIF.RPNM!}</RPNM>
		<MIRS>${RBIF.MIRS!}</MIRS><!-- 新增 -->
	</RBIF>
	<SEIFs>
		<#list SEIFs as SEIF>
		<SEIF seqno="${SEIF.SEQNO!}">
			<CSNM>${SEIF.CSNM!}</CSNM>
			<SEVC>${SEIF.SEVC!}</SEVC><!-- 新增 -->
			<SIIFs>
				<#list CommonFilter.doFilter(SEIF, SIIFs, "P_SEQNO1=P_SEQNO1") as SIIF>
				<SIIF seqno="${SIIF.SEQNO!}">
					<SENM>${SIIF.SENM!}</SENM><!-- 新增 -->
					<SETP>${SIIF.SETP!}</SETP><!-- 新增 -->
					<OITP>${SIIF.OITP!}</OITP><!-- 新增，下有重复，前缀 -->
					<SEID>${SIIF.SEID!}</SEID><!-- 新增 -->
				</SIIF>
				</#list>
			</SIIFs>
			<STNTs>
				<#list CommonFilter.doFilter(SEIF, STNTs, "P_SEQNO1=P_SEQNO1") as STNT>
				<STNT seqno="${STNT.SEQNO!}">${STNT.STNT!}</STNT><!-- 新增 -->
				</#list>
			</STNTs>
			<SCIF>
				<SCTLs>
					<#list CommonFilter.doFilter(SEIF, SCTLs, "P_SEQNO1=P_SEQNO1") as SCTL>
					<SCTL seqno="${SCTL.SEQNO!}">${SCTL.SCTL!}</SCTL><!-- 新增 -->
					</#list>
				</SCTLs>
				<SEARs>
					<#list CommonFilter.doFilter(SEIF, SEARs, "P_SEQNO1=P_SEQNO1") as SEAR>
					<SEAR seqno="${SEAR.SEQNO!}">${SEAR.SEAR!}</SEAR><!-- 新增 -->
					</#list>
				</SEARs>
				<SEEIs>
					<#list CommonFilter.doFilter(SEIF, SEEIs, "P_SEQNO1=P_SEQNO1") as SEEI>
					<SEEI seqno="${SEEI.SEQNO!}">${SEEI.SEEI!}</SEEI><!-- 新增 -->
					</#list>
				</SEEIs>
			</SCIF>
			<SRIF>
				<SRNM>${SEIF.SRNM!}</SRNM><!-- 新增 -->
				<SRIT>${SEIF.SRIT!}</SRIT><!-- 新增 -->
				<ORIT>${SEIF.ORIT!}</ORIT><!-- 新增 -->
				<SRID>${SEIF.SRID!}</SRID><!-- 新增 -->
				<SCNM>${SEIF.SCNM!}</SCNM><!-- 新增 -->
				<SCIT>${SEIF.SCIT!}</SCIT><!-- 新增 -->
				<OCIT>${SEIF.OCIT!}</OCIT><!-- 新增 -->
				<SCID>${SEIF.SCID!}</SCID><!-- 新增 -->
			</SRIF>
		</SEIF>
		</#list>
	</SEIFs>
	<STIFs>
		<#list STIFs as STIF>
		<STIF seqno="${STIF.SEQNO!}">
			<RINI>
				<FINC>${STIF.FINC!}</FINC><!-- 新增 -->
				<RLFC>${STIF.RLFC!}</RLFC><!-- 新增 -->
			</RINI>
			<CIIF>
				<CTNM>${STIF.CTNM!}</CTNM>
				<CITP>${STIF.CITP!}</CITP>
				<OITP>${STIF.OITP!}</OITP><!-- 新增，重复1 -->
				<CTID>${STIF.CTID!}</CTID>
				<CSNM>${STIF.CSNM!}</CSNM>
			</CIIF>
			<ATIF>
				<CATP>${STIF.CATP!}</CATP>
				<CTAC>${STIF.CTAC!}</CTAC>
				<OATM>${STIF.OATM!}</OATM>
				<CATM>${STIF.CATM!}</CATM>
				<CBCT>${STIF.CBCT!}</CBCT><!-- 新增 -->
				<OCBT>${STIF.OCBT!}</OCBT><!-- 新增 -->
				<CBCN>${STIF.CBCN!}</CBCN><!-- 新增 -->
			</ATIF>
			<TBIF>
				<TBNM>${STIF.TBNM!}</TBNM><!-- 新增 -->
				<TBIT>${STIF.TBIT!}</TBIT><!-- 新增 -->
				<OITP>${STIF.OITP!}</OITP><!-- 新增，重复2 -->
				<TBID>${STIF.TBID!}</TBID><!-- 新增 -->
				<TBNT>${STIF.TBNT!}</TBNT><!-- 新增 -->
			</TBIF>
			<TSIF>
				<TSTM>${STIF.TSTM!}</TSTM>
				<TRCD>${STIF.TRCD!}</TRCD>
				<TICD>${STIF.TICD!}</TICD>
				<RPMT>${STIF.RPMT!}</RPMT><!-- 新增 -->
				<RPMN>${STIF.RPMN!}</RPMN><!-- 新增 -->
				<TSTP>${STIF.TSTP!}</TSTP>
				<OCTT>${STIF.OCTT!}</OCTT><!-- 新增 -->
				<OOCT>${STIF.OOCT!}</OOCT><!-- 新增 -->
				<OCEC>${STIF.OCEC!}</OCEC><!-- 新增 -->
				<BPTC>${STIF.BPTC!}</BPTC><!-- 新增 -->
				<TSCT>${STIF.TSCT!}</TSCT>
				<TSDR>${STIF.TSDR!}</TSDR>
				<CRSP>${STIF.CRSP!}</CRSP>
				<CRTP>${STIF.CRTP!}</CRTP>
				<CRAT>${STIF.CRAT!}</CRAT>
			</TSIF>
			<TCIF>
				<CFIN>${STIF.CFIN!}</CFIN>
				<CFCT>${STIF.CFCT!}</CFCT>
				<CFIC>${STIF.CFIC!}</CFIC>
				<CFRC>${STIF.CFRC!}</CFRC>
				<TCNM>${STIF.TCNM!}</TCNM>
				<TCIT>${STIF.TCIT!}</TCIT>
				<OITP>${STIF.OITP!}</OITP><!-- 新增，重复3 -->
				<TCID>${STIF.TCID!}</TCID>
				<TCAT>${STIF.TCAT!}</TCAT>
				<TCAC>${STIF.TCAC!}</TCAC>
			</TCIF>
			<ROTFs>
				<#list CommonFilter.doFilter(STIF, ROTFs, "P_SEQNO1=P_SEQNO1") as ROTF>
				<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF!}</ROTF><!-- 新增 -->
				<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF!}</ROTF><!-- 新增 -->
				</#list>
			</ROTFs>
		</STIF>
		</#list>
	</STIFs>
</BSTR>