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

const toSet = s => new Set([s.split('')]);
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

async function main() {
	const rl = readline.createInterface({
		input: process.stdin,
		output: process.stdout,
		terminal: false
	});

	return new Promise((resolve) => {
		let uniqueOutputs = 0;
		rl.on('line', line => {
			console.log(line);
			const rules = line.split(" | ")[0].split(" ");
			const output = line.split(" | ")[1].split(" ");

			uniqueOutputs += output.filter(rule => rule.length === 2 || rule.length === 3 || rule.length === 4 || rule.length === 7).length; 
			console.log({ rules, output });
		});

		rl.on('close', () => { resolve(uniqueOutputs); });
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
