<?xml version="1.0" encoding="utf©\8"?>
<DHTR>
	<RBIF>
		<RICD>${RBIF.RICD}</RICD>
		<DTTN>${RBIF.DTTN}</DTTN>
	</RBIF>
	<DTDTs>
		<#list DTDTs as DTDT>
		<DTDT seqno="${DTDT.SEQNO}">
			<CSNM>${DTDT.CSNM}</CSNM>
			<HTDT>${DTDT.HTDT}</HTDT>
			<CRCD>${DTDT.CRCD}</CRCD>
			<TICD>${DTDT.TICD}</TICD>
		</DTDT>
		</#list>
	</DTDTs>
</DHTR>