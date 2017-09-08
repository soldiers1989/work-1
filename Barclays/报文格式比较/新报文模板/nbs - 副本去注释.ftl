<?xml version="1.0" encoding="utf©\8"?>
<BSTR>
	<RBIF>
		<RICD>${RBIF.RICD!}</RICD>
		<RPNC>${RBIF.RPNC!}</RPNC>
		<DETR>${RBIF.DETR!}</DETR>
		<TORP>${RBIF.TORP!}</TORP>
		<ORXN>${RBIF.ORXN!}</ORXN>
		<DORP>${RBIF.DORP!}</DORP>
		<ODRP>${RBIF.ODRP!}</ODRP>
		<TPTR>${RBIF.TPTR!}</TPTR>
		<OTPR>${RBIF.OTPR!}</OTPR>
		<STCB>${RBIF.STCB!}</STCB>
		<AOSP>${RBIF.AOSP!}</AOSP>
		<TOSCs>
			<#list TOSCs as TOSC>
			<TOSC seqno="${TOSC.SEQNO!}">${TOSC.TOSC!}</TOSC>
			</#list>
		</TOSCs>
		<STCRs>
			<#list STCRs as STCR>
			<STCR seqno="${STCR.SEQNO!}">${STCR.STCR!}</STCR>
			</#list>
		</STCRs>
		<SETN>${RBIF.SETN!}</SETN>
		<STNM>${RBIF.STNM!}</STNM>
		<RPNM>${RBIF.RPNM!}</RPNM>
		<MIRS>${RBIF.MIRS!}</MIRS>
	</RBIF>
	<SEIFs>
		<#list SEIFs as SEIF>
		<SEIF seqno="${SEIF.SEQNO!}">
			<CSNM>${SEIF.CSNM!}</CSNM>
			<SEVC>${SEIF.SEVC!}</SEVC>
			<SIIFs>
				<#list CommonFilter.doFilter(SEIF, SIIFs, "P_SEQNO1=P_SEQNO1") as SIIF>
				<SIIF seqno="${SIIF.SEQNO!}">
					<SENM>${SIIF.SENM!}</SENM>
					<SETP>${SIIF.SETP!}</SETP>
					<OITP>${SIIF.OITP!}</OITP>
					<SEID>${SIIF.SEID!}</SEID>
				</SIIF>
				</#list>
			</SIIFs>
			<STNTs>
				<#list CommonFilter.doFilter(SEIF, STNTs, "P_SEQNO1=P_SEQNO1") as STNT>
				<STNT seqno="${STNT.SEQNO!}">${STNT.STNT!}</STNT>
				</#list>
			</STNTs>
			<SCIF>
				<SCTLs>
					<#list CommonFilter.doFilter(SEIF, SCTLs, "P_SEQNO1=P_SEQNO1") as SCTL>
					<SCTL seqno="${SCTL.SEQNO!}">${SCTL.SCTL!}</SCTL>
					</#list>
				</SCTLs>
				<SEARs>
					<#list CommonFilter.doFilter(SEIF, SEARs, "P_SEQNO1=P_SEQNO1") as SEAR>
					<SEAR seqno="${SEAR.SEQNO!}">${SEAR.SEAR!}</SEAR>
					</#list>
				</SEARs>
				<SEEIs>
					<#list CommonFilter.doFilter(SEIF, SEEIs, "P_SEQNO1=P_SEQNO1") as SEEI>
					<SEEI seqno="${SEEI.SEQNO!}">${SEEI.SEEI!}</SEEI>
					</#list>
				</SEEIs>
			</SCIF>
			<SRIF>
				<SRNM>${SEIF.SRNM!}</SRNM>
				<SRIT>${SEIF.SRIT!}</SRIT>
				<ORIT>${SEIF.ORIT!}</ORIT>
				<SRID>${SEIF.SRID!}</SRID>
				<SCNM>${SEIF.SCNM!}</SCNM>
				<SCIT>${SEIF.SCIT!}</SCIT>
				<OCIT>${SEIF.OCIT!}</OCIT>
				<SCID>${SEIF.SCID!}</SCID>
			</SRIF>
		</SEIF>
		</#list>
	</SEIFs>
	<STIFs>
		<#list STIFs as STIF>
		<STIF seqno="${STIF.SEQNO!}">
			<RINI>
				<FINC>${STIF.FINC!}</FINC>
				<RLFC>${STIF.RLFC!}</RLFC>
			</RINI>
			<CIIF>
				<CTNM>${STIF.CTNM!}</CTNM>
				<CITP>${STIF.CITP!}</CITP>
				<OITP>${STIF.OITP!}</OITP>
				<CTID>${STIF.CTID!}</CTID>
				<CSNM>${STIF.CSNM!}</CSNM>
			</CIIF>
			<ATIF>
				<CATP>${STIF.CATP!}</CATP>
				<CTAC>${STIF.CTAC!}</CTAC>
				<OATM>${STIF.OATM!}</OATM>
				<CATM>${STIF.CATM!}</CATM>
				<CBCT>${STIF.CBCT!}</CBCT>
				<OCBT>${STIF.OCBT!}</OCBT>
				<CBCN>${STIF.CBCN!}</CBCN>
			</ATIF>
			<TBIF>
				<TBNM>${STIF.TBNM!}</TBNM>
				<TBIT>${STIF.TBIT!}</TBIT>
				<OITP>${STIF.OITP!}</OITP>
				<TBID>${STIF.TBID!}</TBID>
				<TBNT>${STIF.TBNT!}</TBNT>
			</TBIF>
			<TSIF>
				<TSTM>${STIF.TSTM!}</TSTM>
				<TRCD>${STIF.TRCD!}</TRCD>
				<TICD>${STIF.TICD!}</TICD>
				<RPMT>${STIF.RPMT!}</RPMT>
				<RPMN>${STIF.RPMN!}</RPMN>
				<TSTP>${STIF.TSTP!}</TSTP>
				<OCTT>${STIF.OCTT!}</OCTT>
				<OOCT>${STIF.OOCT!}</OOCT>
				<OCEC>${STIF.OCEC!}</OCEC>
				<BPTC>${STIF.BPTC!}</BPTC>
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
				<OITP>${STIF.OITP!}</OITP>
				<TCID>${STIF.TCID!}</TCID>
				<TCAT>${STIF.TCAT!}</TCAT>
				<TCAC>${STIF.TCAC!}</TCAC>
			</TCIF>
			<ROTFs>
				<#list CommonFilter.doFilter(STIF, ROTFs, "P_SEQNO1=P_SEQNO1") as ROTF>
				<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF!}</ROTF>
				<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF!}</ROTF>
				</#list>
			</ROTFs>
		</STIF>
		</#list>
	</STIFs>
</BSTR>