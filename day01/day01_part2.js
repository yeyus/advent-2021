const stdinReducer = require('./stdin-reducer');

async function main() {
    const result = await stdinReducer({
        initialState: { 
            prev: 0, 
            w: [], 
            num: 0, 
            increasing: 0 
        },
        reducer: ({ prev, w, num,increasing }, reading) => {

            if (w.length == 3) {
                // rotate for window size 3
                w = [...w.slice(1), reading];
            } else {
                // add for window < 3
                w = [...w, reading];
            }
            
            
            const wSum = w.reduce((prev, current) => prev + current, 0);
            //console.log(`Window is ${w} with sum ${wSum}`);

            if (num > 0 && wSum > prev) {
                increasing++;
            }
            
            prev = wSum;
            if (w.length >= 3) {
                num++;
            }
        
            return { prev, w, num, increasing };
        }
    });

    console.log(result.increasing);
}

(async () => {
    try {
        await main();
    } catch (e) {
        console.error(e);
    }
})();