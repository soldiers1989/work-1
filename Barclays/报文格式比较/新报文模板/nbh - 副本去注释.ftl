<?xml version="1.0" encoding="utf©\8"?>
<HVTR>
	<RBIF>
		<RICD>${RBIF.RICD!}</RICD>
		<CTTN>${RBIF.CTTN!}</CTTN>
	</RBIF>
	<CATIs>
		<#list CATIs as CATI>
		<CATI seqno="${CATI.SEQNO!}">
			<CBIF>
				<CSNM>${CATI.CSNM!}</CSNM>
				<CTNTs>
					<#list CommonFilter.doFilter(CATI, CTNTs, "P_SEQNO1=P_SEQNO1") as CTNT>
					<CTNT seqno="${CTNT.SEQNO!}">${CTNT.CTNT!}</CTNT>
					</#list>
				</CTNTs>
				<CTVC>${CATI.CTVC!}</CTVC>
				<CCIF>
					<CCTLs>
						<#list CommonFilter.doFilter(CATI, CCTLs, "P_SEQNO1=P_SEQNO1") as CCTL>
						<CCTL seqno="${CCTL.SEQNO!}">${CCTL.CCTL!}</CCTL>
						</#list>
					</CCTLs>
					<CATRs>
						<#list CommonFilter.doFilter(CATI, CATRs, "P_SEQNO1=P_SEQNO1") as CTAR>
						<CTAR seqno="${CTAR.SEQNO!}">${CTAR.CTAR!}</CTAR>
						</#list>
					</CATRs>
					<CCEIs>
						<#list CommonFilter.doFilter(CATI, CCEIs, "P_SEQNO1=P_SEQNO1") as CCEI>
						<CCEI seqno="${CCEI.SEQNO!}">${CCEI.CCEI!}</CCEI>
						</#list>
					</CCEIs>
				</CCIF>
			</CBIF>
			<HTDT>${CATI.HTDT!}</HTDT>
			<HTCRs>
				<#list CommonFilter.doFilter(CATI, HTCRs, "P_SEQNO1=P_SEQNO1") as HTCR>
				<HTCR seqno="${HTCR.SEQNO!}">
					<CRCD>${HTCR.CRCD!}</CRCD>
					<TTNM>${HTCR.TTNM!}</TTNM>
					<CCIFs>
						<#list CommonFilter.doFilter(HTCR, CCIFs, "P_SEQNO2=P_SEQNO2","P_SEQNO1=P_SEQNO1") as CCIF>
						<CCIF seqno="${CCIF.SEQNO!}">
							<CTNM>${CATI.CTNM!}</CTNM>
							<CITP>${CATI.CITP!}</CITP>
							<OITP>${CATI.OITP!}</OITP>
							<CTID>${CATI.CTID!}</CTID>
							<TSDTs>
								<#list CommonFilter.doFilter(HTCR, TSDTs, "P_SEQNO2=P_SEQNO2","P_SEQNO1=P_SEQNO1","P_SEQNO3=P_SEQNO3") as TSDT>
								<TSDT seqno="${TSDT.SEQNO!}">
									<RINI>
										<FINC>${TSDT.FINC!}</FINC>
										<RLFC>${TSDT.RLFC!}</RLFC>
									</RINI>
									<ATIF>
										<CATP>${TSDT.CATP!}</CATP>
										<CTAC>${TSDT.CTAC!}</CTAC>
										<OATM>${TSDT.OATM!}</OATM>
										<CBCT>${TSDT.CBCT!}</CBCT>
										<OCBT>${TSDT.OCBT!}</OCBT>
										<CBCN>${TSDT.CBCN!}</CBCN>
									</ATIF>
									<TBIF>
										<TBNM>${TSDT.TBNM!}</TBNM>
										<TBIT>${TSDT.TBIT!}</TBIT>
										<OITP>${TSDT.OITP!}</OITP>
										<TBID>${TSDT.TBID!}</TBID>
										<TBNT>${TSDT.TBNT!}</TBNT>
									</TBIF>
									<TSIF>
										<TSTM>${TSDT.TSTM!}</TSTM>
										<TRCD>${TSDT.TRCD!}</TRCD>
										<TICD>${TSDT.TICD!}</TICD>
										<RPMT>${TSDT.RPMT!}</RPMT>
										<RPMN>${TSDT.RPMN!}</RPMN>
										<TSTP>${TSDT.TSTP!}</TSTP>
										<OCTT>${TSDT.OCTT!}</OCTT>
										<OOCT>${TSDT.OOCT!}</OOCT>
										<OCEC>${TSDT.OCEC!}</OCEC>
										<BPTC>${TSDT.BPTC!}</BPTC>
										<TSCT>${TSDT.TSCT!}</TSCT>
										<TSDR>${TSDT.TSDR!}</TSDR>
										<CRPP>${TSDT.CRPP!}</CRPP>
										<CRTP>${TSDT.CRTP!}</CRTP>
										<CRAT>${TSDT.CRAT!}</CRAT>
										<CRMB>${TSDT.CRMB!}</CRMB>
										<CUSD>${TSDT.CUSD!}</CUSD>
									</TSIF>
									<TCIF>
										<CFIN>${TSDT.CFIN!}</CFIN>
										<CFCT>${TSDT.CFCT!}</CFCT>
										<CFIC>${TSDT.CFIC!}</CFIC>
										<CFRC>${TSDT.CFRC!}</CFRC>
										<TCNM>${TSDT.TCNM!}</TCNM>
										<TCIT>${TSDT.TCIT!}</TCIT>
										<OITP>${TSDT.OITP!}</OITP>
										<TCID>${TSDT.TCID!}</TCID>
										<TCAT>${TSDT.TCAT!}</TCAT>
										<TCAC>${TSDT.TCAC!}</TCAC>
									</TCIF>
									<ROTFs>
										<#list CommonFilter.doFilter(HTCR, ROTFs, "P_SEQNO2=P_SEQNO2","P_SEQNO1=P_SEQNO1",,"P_SEQNO3=P_SEQNO3")) as ROTF>
										<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF!}</ROTF>
										<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF!}</ROTF>
										</#list>
									</ROTFs>
								</TSDT>
								</#list>
							</TSDTs>
						</CCIF>
						</#list>
					</CCIFs>
				</HTCR>
				</#list>
			</HTCRs>
		</CATI>
		</#list>
	</CATIs>
</HVTR>