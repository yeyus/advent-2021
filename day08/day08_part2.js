#!/usr/bin/env node

const readline = require('readline');

/*
 * Original 7-segment display
 *  aaaa
 * f    b
 * f    b
 *  gggg
 * e    c
 * e    c
 *  dddd
 * 
 * 0 = abcdef
 * 1 = bc
 * 2 = abdeg
 * 3 = abcdg
 * 4 = bcfg
 * 5 = acdfg
 * 6 = acdefg
 * 7 = abc
 * 8 = abcdefg
 * 9 = abcdfg
 *
 * 1,4,7,8 have no conflicts
 * 0,6,9 have 6 segments on an vary only 1 segment (abcdef (!g), acdefg(!b), abcdfg (!e))
 * 2,3,5 have 5 segments an vary in 2 segment (abdeg, abcdg, acdfg)
 *
 * a = 0,2,3,5,6,7,8,9
 * b = 0,1,2,3,4,7,8,9
 * c = 0,1,3,4,5,6,7,8,9
 * d = 0,2,3,5,6,8,9
 * e = 0,2,6,8
 * f = 0,4,5,6,8,9
 * g = 2,3,4,5,6,8,9
*/

const toSet = s => new Set([...s.split('')]);
const OG = [
	toSet('abcdef'),  // 0
	toSet('bc'),      // 1
	toSet('abdeg'),   // 2
	toSet('abcdg'),   // 3
	toSet('bcfg'),    // 4
	toSet('acdfg'),   // 5
	toSet('acdefg'),  // 6
	toSet('abc'),     // 7
	toSet('abcdefg'), // 8
	toSet('abcdfg')   // 9
];

const setMinus = (a, b) => new Set([...a].filter(x => !b.has(x)));
const setEqual = (a, b) => {
	if (a.size != b.size) return false;
	for(let x of a) {
		if (!b.has(x)) return false;
	}

	return true;
};

function guess(rules) {
	const map = {};
	const relate = (og, s) => s.forEach(v => map[v] = og);

	// create frequency array
	const freq = ['a', 'b', 'c', 'd', 'e', 'f', 'g'].reduce((prev, cur) => ({...prev, [cur]: 0}), {});
	rules.forEach(rule => Array.from(rule).forEach(ch => {
		freq[ch] += 1;
	}));

	// guess segment a
	const group7 = toSet(rules.find(r => r.length === 3));
	const group1 = toSet(rules.find(r => r.length === 2));
	const group4 = toSet(rules.find(r => r.length === 4));
	let identified = new Set();

	const a = setMinus(group7, group1);
	identified = new Set([...identified, ...a]);
	relate('a', a);

	const b = new Set(Object.keys(freq).filter(k => freq[k] === 8 && !a.has(k)));
	identified = new Set([...identified, ...b]);
	relate('b', b);

	const c = new Set(Object.keys(freq).filter(k => freq[k] === 9));
	identified = new Set([...identified, ...c]);
	relate('c', c);

	const e = new Set(Object.keys(freq).filter(k => freq[k] === 4));
	identified = new Set([...identified, ...e]);
	relate('e', e);

	const f = new Set(Object.keys(freq).filter(k => freq[k] === 6));
	identified = new Set([...identified, ...f]);
	relate('f', f);

	const g = setMinus(group4, identified);
	identified = new Set([...identified, ...g]);
	relate('g', g);

	const d = setMinus(OG[8], identified);
	relate('d', d);

	return map;
}

function decode(mappings, output) {
	const ogSegment = new Set(Array.from(output).map(ch => mappings[ch]));
	return OG.findIndex(o => setEqual(o, ogSegment));	
}

async function main() {
	const rl = readline.createInterface({
		input: process.stdin,
		output: process.stdout,
		terminal: false
	});

	return new Promise((resolve) => {
		let sum = 0;
		rl.on('line', line => {
			//console.log(line);
			const rules = line.split(" | ")[0].split(" ");
			const output = line.split(" | ")[1].split(" ");

			// we take the rules and we build a translation dictionary
			const mappings = guess(rules);

			// we take the outputs and decode them with such dictionary
			const digits = output.map(o => decode(mappings, o));
			const number = Number.parseInt(digits.map(d => d+'').join(''), 10);
			console.log("digits", digits, "number", number);
			
			sum += number;
		});

		rl.on('close', () => { resolve(sum); });
	});
}

(async () => {
    try {
        const result = await main();
				console.log(`The result is ${result}`);
    } catch (e) {
        console.error(e);
    }
})();
