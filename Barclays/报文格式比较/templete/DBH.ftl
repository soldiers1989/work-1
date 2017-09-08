<?xml version="1.0" encoding="gb18030"?>
<DHTR>
	<RBIF>
		<RINM>${RBIF.RINM}</RINM>
		<RICD>${RBIF.RICD}</RICD>
		<DRDT>${RBIF.DRDT}</DRDT>
		<DTTN>${RBIF.DTTN}</DTTN>
	</RBIF>
	<DTDTs>
		<#list DTDTs as DTDT>
		<DTDT seqno="${DTDT.SEQNO}">
			<HTDT>${DTDT.HTDT}</HTDT>
			<CSNM>${DTDT.CSNM}</CSNM>
			<CRCD>${DTDT.CRCD}</CRCD>
			<TICD>${DTDT.TICD}</TICD>
		</DTDT>
		</#list>
	</DTDTs>
</DHTR>
