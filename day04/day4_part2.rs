use std::io;
use std::io::BufRead;
use std::cmp;

mod bingocard;
use bingocard::BingoCard;

fn main() {
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
    
    let mut max_num = 0;
    let mut last_card_index = 0;

    for i in 0 .. cards.len() {
        let c = &mut cards[i];
        'inner: for j in 0 .. numbers.len() {
            let number = numbers[j];
            let result = c.offer(number);
            match result {
                Some(_) => {
                    max_num = cmp::max(max_num, j);
                    
                    if max_num == j {
                        last_card_index = i;
                    }
                    break 'inner;
                },
                None => (),
            }
        }
    }

    println!("Last card is {:?}", cards[last_card_index]);
    println!("Last card result is {}", cards[last_card_index].sum() * numbers[max_num]);
    println!("Last number is {}", numbers[max_num]);

}
