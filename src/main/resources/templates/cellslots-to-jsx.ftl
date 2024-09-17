<#assign code = v.code><#t>
<#assign i = 0><#t>
<#assign start = 0><#t>
<#assign size = v.size><#t>
<#assign end = v.end><#t>
<#list (v.cellslots) as c><#t>
${f.substr(code, start, end)}<#t>
${c.content}<#t>
	<#assign start = c.end><#t>
	<#assign i = i+1><#t>
	<#if i lt size><#t>
		<#assign end = v.cellslots[i].start><#t>
	</#if><#t>
</#list><#t>
${ f.substr(code, (v.cellslots[size-1]).end, code?length)}<#t>