// comment
const slot1 =
<Row>
	<Col size="4">
		<Data text="blahblah" number="42" />
	</Col>
	<Col size="8">
		<Row>
			<Col size="6">
				<Data number="42" />
				<Data2 number="42" text="blahblah" />
			</Col>
			<Col size="6">
				<Data2 number="42" text="blahblah" />
				<Data2 number="42" text="blahblah" />
			</Col>
		</Row>
	</Col>
</Row>;
ReactDOM.render(slot1, document.getElementById('root'));
