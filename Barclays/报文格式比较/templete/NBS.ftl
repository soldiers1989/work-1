<?xml version="1.0" encoding="gb18030"?>
<BSTR>
	<RBIF>
		<FINM>${RBIF.FINM!}</FINM>
		<FIRC>${RBIF.FIRC!}</FIRC>
		<FICT>${RBIF.FICT!}</FICT>
		<FICD>${RBIF.FICD!}</FICD>
		<STCD>
			<STCR>${RBIF.STCR!}</STCR>
			<SSDG>${RBIF.SSDG!}</SSDG>
			<TKMS>${RBIF.TKMS!}</TKMS>
			<SSDS>${RBIF.SSDS!}</SSDS>
		</STCD>
		<CTTN>${RBIF.CTTN!}</CTTN>
		<TTNM>${TTNM.TTNM!}</TTNM>
		<RPNM>${RBIF.RPNM!}</RPNM>
		<RPDT>${RBIF.RPDT!}</RPDT>
	</RBIF>
	<CTIFs>
		<#list CTIFs as CTIF>
		<CTIF seqno="${CTIF.SEQNO!}">
			<CTNM>${CTIF.CTNM!}</CTNM>
			<CITP>${CTIF.CITP!}</CITP>
			<CTID>${CTIF.CTID!}</CTID>
			<CSNM>${CTIF.CSNM!}</CSNM>
			<CTTP>${CTIF.CTTP!}</CTTP>
			<CCIF>
				<CCTL>${CTIF.CCTL!}</CCTL>
				<CTAR>${CTIF.CTAR!}</CTAR>
				<CCEI>${CTIF.CCEI!}</CCEI>
			</CCIF>
			<CTNT>${CTIF.CTNT!}</CTNT>
			<CTVC>${CTIF.CTVC!}</CTVC>
			<RGCP>${CTIF.RGCP!}</RGCP>
			<CRNM>${CTIF.CRNM!}</CRNM>
			<CRIT>${CTIF.CRIT!}</CRIT>
			<CRID>${CTIF.CRID!}</CRID>
			<ATNM>${CTIF.ATNM!}</ATNM>
			<ATIFs>
				<#list CommonFilter.doFilter(CTIF, ATIFs, "P_SEQNO1=P_SEQNO1") as ATIF>
				<ATIF seqno="${ATIF.SEQNO!}">
					<CATP>${ATIF.CATP!}</CATP>
					<CTAC>${ATIF.CTAC!}</CTAC>
					<OATM>${ATIF.OATM!}</OATM>
					<CATM>${ATIF.CATM!}</CATM>
				</ATIF>
				</#list>
			</ATIFs>
		</CTIF>
		</#list>
	</CTIFs>
	<RPDIs>
		<#list RPDIs as RPDI>
		<RPDI seqno="${RPDI.SEQNO!}">
			<CTNM>${RPDI.CTNM!}</CTNM>
			<CITP>${RPDI.CITP!}</CITP>
			<CTID>${RPDI.CTID!}</CTID>
			<CTAC>${RPDI.CTAC!}</CTAC>
			<BKIF>
				<BKNM>${RPDI.BKNM!}</BKNM>
				<BITP>${RPDI.BITP!}</BITP>
				<BKID>${RPDI.BKID!}</BKID>
				<BKNT>${RPDI.BKNT!}</BKNT>
			</BKIF>
			<TSIF>
				<TSTM>${RPDI.TSTM!}</TSTM>
				<TRCD>${RPDI.TRCD!}</TRCD>
				<TICD>${RPDI.TICD!}</TICD>
				<TSTP>${RPDI.TSTP!}</TSTP>
				<TSCT>${RPDI.TSCT!}</TSCT>
				<TSDR>${RPDI.TSDR!}</TSDR>
				<CRSP>${RPDI.CRSP!}</CRSP>
				<CRTP>${RPDI.CRTP!}</CRTP>
				<CRAT>${RPDI.CRAT!}</CRAT>
			</TSIF>
			<TCIF>
				<CFIN>${RPDI.CFIN!}</CFIN>
				<CFCT>${RPDI.CFCT!}</CFCT>
				<CFIC>${RPDI.CFIC!}</CFIC>
				<CFRC>${RPDI.CFRC!}</CFRC>
				<TCNM>${RPDI.TCNM!}</TCNM>
				<TCIT>${RPDI.TCIT!}</TCIT>
				<TCID>${RPDI.TCID!}</TCID>
				<TCAT>${RPDI.TCAT!}</TCAT>
				<TCAC>${RPDI.TCAC!}</TCAC>
			</TCIF>
		</RPDI>
		</#list>
	</RPDIs>
</BSTR>
