use std::io;
use std::io::BufRead;

mod bingocard;
use bingocard::BingoCard;

fn main() {
    //println!("Hello World!");

    let stdin = io::stdin();
    
    let mut n = 0;
    let mut numbers: Vec<_> = Vec::new();
    let mut cards: Vec<BingoCard> = Vec::new();
    let mut card = Vec::new();

    // Start the parsing of the input
    for line in stdin.lock().lines() {
        let input = line.expect("Failed to read line");

        // process list of numbers
        if n == 0 {
            numbers = input
                .split(",")
                .into_iter()
                .filter_map(|s| s.parse::<u32>().ok())
                .collect();
            //println!("Numbers: {:?}", numbers); 
        }

        if n > 1 {
            if input.trim().chars().count() == 0 {
                cards.push(BingoCard::new(card));
                card = Vec::new();
            } else {
                let v: Vec<_> = input
                    .split(" ")
                    .into_iter()
                    .filter_map(|s| s.parse::<u32>().ok())
                    .collect();
                card.push(v);
            }
        }

        //println!("{}", input);
        n += 1;
    }

    // lets play bingo
    println!("Number of parsed BingoCards: {}", cards.len());

    'outer: for number in numbers {
        for c in cards.iter_mut() {
            let result = c.offer(number);
            match result {
                Some(x) => {
                    println!("Result: {}", x);
                    println!("Result card: {:?}", c);
                    break 'outer;
                },
                None => (),
            }
        }
    }
}
