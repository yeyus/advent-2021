use std::io;
use std::io::BufRead;
use std::collections::HashSet;

#[derive(Debug)]
struct BingoCard { rows: Vec<HashSet<u32>>, columns: Vec<HashSet<u32>> }

impl BingoCard {
    fn new(card: Vec<Vec<u32>>) -> BingoCard {
        let mut r: Vec<_> = (0..5)
            .map(|_| HashSet::<u32>::new())
            .collect();

        let mut c: Vec<_> = (0..5)
            .map(|_| HashSet::<u32>::new())
            .collect();

        for i in 0 .. card.len() {
            for j in 0 .. card[i].len() {
                r[i].insert(card[i][j]);
            }
        }
        
        for i in 0 .. card.len() {
            for j in 0 .. card[i].len() {
                c[j].insert(card[i][j]);        
            }
        }

        BingoCard { rows: r, columns: c }
    }

    fn offer(&mut self, n: u32) -> Option<u32> {
        let mut bingo = false;

        for row in self.rows.iter_mut() {
            row.remove(&n);
            if row.len() == 0 {
                bingo = true;                   
            }
        }
        
        for column in self.columns.iter_mut() {
            column.remove(&n);
            if column.len() == 0 {
                bingo = true;                   
            }
        }
        
        if bingo { Some(self.sum() * n) } else { None }
    }

    fn sum(&self) -> u32 {
        self.rows.iter().flatten().sum::<u32>()
    }
}

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
