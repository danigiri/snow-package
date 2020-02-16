// comment
const slot1 =
<row>
	<col size="4">
		<data text="blahblah" number="42" />
	</col>
	<col size="8">
		<row>
			<col size="6">
				<data number="42" />
				<data2 number="42" text="blahblah" />
			</col>
			<col size="6">
				<data2 number="42" text="blahblah" />
				<data2 number="42" text="blahblah" />
			</col>
		</row>
	</col>
</row>;
ReactDOM.render(slot1, document.getElementById('root'));
