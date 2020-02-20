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


constructor(private src: string) {

}


//// SlotExtractor ////

extract(): string {

	let out = '[';

	// we ignore this as the ParserPlugin type is closed and does not list plugins
	// @ts-ignore
	const _plugins: ParserPlugin[] = ['jsx', '@babel/plugin-transform-react-jsx-source'];

	this.ast = parse(this.src, {plugins: _plugins});
	//console.debug(this.ast);

	var jsxStackingElementCounter = 0;

	traverse(this.ast, {
		enter(path: NodePath) {	// not sure if this is the right type
			const node = path.node;
			const _isJSXOpeningElement = isJSXOpeningElement(node); 
			const _isJSXOpeningFragment = isJSXOpeningFragment(node); 
			let isClosing = isJSXClosingElement(node) || isJSXClosingFragment(node);
			if ( _isJSXOpeningElement || _isJSXOpeningFragment) {
				if (jsxStackingElementCounter++==0) {
					const name = _isJSXOpeningElement ? "___code" : '___fragment';
					out += '{"type":"'+name+'", "start":"'+node.start+'", ';
				}
				isClosing = node.selfClosing === true;	// this is true if we have <foo />
			}
			if (isClosing) {
				if (--jsxStackingElementCounter==0) {
					out += '"end":"'+node.end+'"},\n';
				}
			}

		}
	});

	// remove last ',\n' and close the array
	out = out.slice(0, -2)+']';

	return out;
}




}
