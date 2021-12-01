const readline = require('readline');

/** 
 * stdinReducer will read stdin line by line until EOF and apply the reducer to the lines.
 * @param initialState the initial state value to pass the reducer
 * @param reducer a function that will be called on each line, 
 *      it receives the current state as the first argument and the reading as the second.
 * 
 * @returns a promise that will resolve into the final state on EOF.
 */
function stdinReducer({ initialState, reducer }) {
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout,
        terminal: false
    });

    return new Promise((resolve) => {
        let state = initialState;

        rl.on('line', line => {
            const reading = Number.parseInt(line, 10);
            state = reducer(state, reading);
        });
        
        rl.on('close', () => {
            resolve(state);
        });
    });
};

module.exports = stdinReducer;