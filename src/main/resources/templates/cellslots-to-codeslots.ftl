<#setting number_format="computer"><#t>
<#assign cellslots = v.cellSlots><#t>
<#assign code = v.code><#t>
<#list cellslots as c><#rt>
	<#assign start = c.start><#t>
	<#assign end = c.end><#t>
<codeslot type="${c.type}" id="${c?index}" start="${start}" end="${end}">${ f.substr(code, start, end) }</codeslot>
</#list>
