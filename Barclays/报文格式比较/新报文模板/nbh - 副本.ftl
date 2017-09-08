<?xml version="1.0" encoding="utf‐8"?>
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
					<CTNT seqno="${CTNT.SEQNO!}">${CTNT.CTNT!}</CTNT><!-- 旧版只有一个 -->
					</#list>
				</CTNTs>
				<CTVC>${CATI.CTVC!}</CTVC><!-- 新增 -->
				<CCIF>
					<CCTLs>
						<#list CommonFilter.doFilter(CATI, CCTLs, "P_SEQNO1=P_SEQNO1") as CCTL>
						<CCTL seqno="${CCTL.SEQNO!}">${CCTL.CCTL!}</CCTL><!-- 新增 -->
						</#list>
					</CCTLs>
					<CATRs>
						<#list CommonFilter.doFilter(CATI, CATRs, "P_SEQNO1=P_SEQNO1") as CTAR>
						<CTAR seqno="${CTAR.SEQNO!}">${CTAR.CTAR!}</CTAR><!-- 新增 -->
						</#list>
					</CATRs>
					<CCEIs>
						<#list CommonFilter.doFilter(CATI, CCEIs, "P_SEQNO1=P_SEQNO1") as CCEI>
						<CCEI seqno="${CCEI.SEQNO!}">${CCEI.CCEI!}</CCEI><!-- 新增 -->
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
							<OITP>${CATI.OITP!}</OITP><!-- 新增，下有重复，前缀要改 -->
							<CTID>${CATI.CTID!}</CTID>
							<TSDTs>
								<#list CommonFilter.doFilter(HTCR, TSDTs, "P_SEQNO2=P_SEQNO2","P_SEQNO1=P_SEQNO1") as TSDT>
								<TSDT seqno="${TSDT.SEQNO!}">
									<RINI>
										<FINC>${TSDT.FINC!}</FINC>
										<RLFC>${TSDT.RLFC!}</RLFC><!-- 新增 -->
									</RINI>
									<ATIF>
										<CATP>${TSDT.CATP!}</CATP>
										<CTAC>${TSDT.CTAC!}</CTAC>
										<OATM>${TSDT.OATM!}</OATM><!-- 新增 -->
										<CBCT>${TSDT.CBCT!}</CBCT><!-- 新增 -->
										<OCBT>${TSDT.OCBT!}</OCBT><!-- 新增 -->
										<CBCN>${TSDT.CBCN!}</CBCN><!-- 新增 -->
									</ATIF>
									<TBIF>
										<TBNM>${TSDT.TBNM!}</TBNM>
										<TBIT>${TSDT.TBIT!}</TBIT>
										<OITP>${TSDT.OITP!}</OITP><!-- 新增，重复1 -->
										<TBID>${TSDT.TBID!}</TBID>
										<TBNT>${TSDT.TBNT!}</TBNT>
									</TBIF>
									<TSIF>
										<TSTM>${TSDT.TSTM!}</TSTM>
										<TRCD>${TSDT.TRCD!}</TRCD>
										<TICD>${TSDT.TICD!}</TICD>
										<RPMT>${TSDT.RPMT!}</RPMT><!-- 新增 -->
										<RPMN>${TSDT.RPMN!}</RPMN><!-- 新增 -->
										<TSTP>${TSDT.TSTP!}</TSTP>
										<OCTT>${TSDT.OCTT!}</OCTT><!-- 新增 -->
										<OOCT>${TSDT.OOCT!}</OOCT><!-- 新增 -->
										<OCEC>${TSDT.OCEC!}</OCEC><!-- 新增 -->
										<BPTC>${TSDT.BPTC!}</BPTC><!-- 新增 -->
										<TSCT>${TSDT.TSCT!}</TSCT>
										<TSDR>${TSDT.TSDR!}</TSDR>
										<CRPP>${TSDT.CRPP!}</CRPP>
										<CRTP>${TSDT.CRTP!}</CRTP>
										<CRAT>${TSDT.CRAT!}</CRAT>
										<CRMB>${TSDT.CRMB!}</CRMB><!-- 新增 -->
										<CUSD>${TSDT.CUSD!}</CUSD><!-- 新增 -->
									</TSIF>
									<TCIF>
										<CFIN>${TSDT.CFIN!}</CFIN>
										<CFCT>${TSDT.CFCT!}</CFCT>
										<CFIC>${TSDT.CFIC!}</CFIC>
										<CFRC>${TSDT.CFRC!}</CFRC><!-- 新增 -->
										<TCNM>${TSDT.TCNM!}</TCNM>
										<TCIT>${TSDT.TCIT!}</TCIT>
										<OITP>${TSDT.OITP!}</OITP><!-- 新增，重复2 -->
										<TCID>${TSDT.TCID!}</TCID>
										<TCAT>${TSDT.TCAT!}</TCAT>
										<TCAC>${TSDT.TCAC!}</TCAC>
									</TCIF>
									<ROTFs>
										<#list CommonFilter.doFilter(HTCR, ROTFs, "P_SEQNO2=P_SEQNO2","P_SEQNO1=P_SEQNO1") as ROTF>
										<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF!}</ROTF><!-- 新增 -->
										<ROTF seqno="${ROTF.SEQNO!}">${ROTF.ROTF!}</ROTF><!-- 新增 -->
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