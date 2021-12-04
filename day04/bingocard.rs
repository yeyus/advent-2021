use std::collections::HashSet;

#[derive(Debug)]
pub struct BingoCard { rows: Vec<HashSet<u32>>, columns: Vec<HashSet<u32>> }

impl BingoCard {
    pub fn new(card: Vec<Vec<u32>>) -> BingoCard {
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

    pub fn offer(&mut self, n: u32) -> Option<u32> {
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

    pub fn sum(&self) -> u32 {
        self.rows.iter().flatten().sum::<u32>()
    }
}

