const stdinReducer = require('./stdin-reducer');

async function main() {
    const result = await stdinReducer({
        initialState: { 
            prev: 0, 
            num: 0, 
            increasing: 0 
        },
        reducer: ({ prev, num, increasing }, reading) => {
            if (num > 0 && reading > prev) {
                increasing++;
            }

            prev = reading;
            num++;

            return { prev, num, increasing };
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