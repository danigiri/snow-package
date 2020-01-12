// JSX PACKAGE . CLASS . TS

import { File, Node, isIdentifier } from '@babel/types';
import { parse, ParserPlugin } from '@babel/parser';
import { traverse, NodePath } from '@babel/core';
import { isJSXElement, isJSXIdentifier } from '@babel/types';

import { SlotExtractor } from '../slot-extractor.interface';

export class JSXPackage implements SlotExtractor {

//private readonly JSX_PREFIX = 'JSX';

ast: File;


constructor(private src: string) {

}


//// SlotExtractor ////

extract(): string {

	let out = '';

	// we ignore this as the ParserPlugin type is closed and does not list plugins
	// @ts-ignore
	const _plugins: ParserPlugin[] = ['jsx', '@babel/plugin-transform-react-jsx-source'];

	this.ast = parse(this.src, {plugins: _plugins});

	traverse(this.ast, {
		enter(path: NodePath) {	// not sure if this is the right type
			const node = path.node;
			if (isJSXIdentifier(node)) { //}path.node.type.startsWith('JSX')) {
				console.debug(path.type);
				out += node.name+'\n';
			}

		}
	});

	return out;
}

}
