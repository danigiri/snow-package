// JSX PACKAGE . CLASS . TS

import { File, Node, isIdentifier, isJSXElement, isJSXIdentifier, isJSX, isJSXOpeningElement, isJSXClosingElement,
		isJSXOpeningFragment, isJSXClosingFragment } from '@babel/types';
import { parse, ParserPlugin } from '@babel/parser';
import { traverse, NodePath } from '@babel/core';
import {  } from '@babel/types';

import { SlotExtractor } from '../slot-extractor.interface';

export class JSXPackage implements SlotExtractor {

//private readonly JSX_PREFIX = 'JSX';

ast: File;


constructor(private src: string, private verbose: boolean = false) {

}


//// SlotExtractor ////

extract(): string {

	let out = '[';

	// we ignore this as the ParserPlugin type is closed and does not list plugins
	// @ts-ignore
	const _plugins: ParserPlugin[] = ['jsx', '@babel/plugin-transform-react-jsx-source'];

	//TODO: we may need to introduce a parameter of the source type so it's user configurable'
	this.ast = parse(this.src, {plugins: _plugins, sourceType: 'module'});
	//console.debug(this.ast);

	var jsxStackingElementCounter = 0;
	var _verbose = this.verbose;

	traverse(this.ast, {
		enter(path: NodePath) {	// not sure if this is the right type

			const node = path.node;
			const _isJSXOpeningElement = isJSXOpeningElement(node); 
			const _isJSXOpeningFragment = isJSXOpeningFragment(node);
			const opening = _isJSXOpeningElement || _isJSXOpeningFragment;
			const _isJSXClosinglement = isJSXClosingElement(node) 
			const _isJSXClosingFragment = isJSXClosingFragment(node) 
			const _selfClosing = node.selfClosing === true;	// this is true if we have <foo />
			const closing = _isJSXClosinglement || _isJSXClosingFragment || (opening && _selfClosing);
			if (opening) {
				if (jsxStackingElementCounter++==0) {
					_verbose && console.error(' ****** Opening: ', node.loc.start, node.loc.end);
					const name = _isJSXOpeningElement ? "___code" : '___fragment';
					const line = node.loc.start.line;
					const col = _isJSXOpeningFragment ? node.loc.start.column+2 : node.loc.start.column; // skip '<>'
					out += '{"type":"'+name+'", "start_line":"'+line+'", "start_column":"'+col+'", ';
				}
			}
			if (closing) {
				if (--jsxStackingElementCounter==0) {
					_verbose && console.error(' ****** Closing: ', node.loc.start, node.loc.end);
					const line = node.loc.end.line;
					const col = _isJSXOpeningFragment || _isJSXClosingFragment 
									? node.loc.end.column-3 : node.loc.end.column; // skip '</>' on fragments
					out += '"end_line":"'+line+'", "end_column":"'+col+'"},\n';
				}
			}

		}
	});

	// remove last ',\n' and close the array
	out = out.slice(0, -2)+']';

	return out;
}




}
