use std::io;
use std::io::BufRead;
use std::collections::HashMap;

fn string_to_pairs(string : &String, pairs : &mut HashMap<String, u64>) {
  let mut prev : char = string.chars().nth(0).unwrap();
  for (i,c) in string.chars().enumerate() {
    if i == 0 { continue }

    let pair = format!("{}{}", prev, c);
    let counter = pairs.entry(pair).or_insert(0);
    *counter += 1;

    prev = c;
  }
}

fn print_ch_count(pairs : &mut HashMap<String, u64>) {
  let mut freq : HashMap<char, u64> = HashMap::new();

  for (key, value) in pairs {
    let first = key.chars().nth(0).unwrap();
    let second = key.chars().nth(1).unwrap();

    let fcnt = freq.entry(first).or_insert(0);
    *fcnt += *value;
    let scnt = freq.entry(second).or_insert(0);
    *scnt += *value;
  }

  let mut max_value = f64::MIN;
  let mut min_value = f64::MAX;
  for (_, value) in &freq {
    let v = (*value as f64 / 2f64).ceil();
    if v > max_value {
      max_value = v;
    }

    if v < min_value {
      min_value = v;
    }
  }

  println!("Count of chars: {:?}", freq);
  println!("Result is {} - {} = {}", max_value, min_value, max_value - min_value);
}

fn main() {
  let stdin = io::stdin();

  let mut is_first_line = true;
  let mut pairs = HashMap::new();
  let mut insertions = HashMap::new();

  for line in stdin.lock().lines() {

    let input = line.unwrap();

    // skip empty lines
    if input.len() == 0 {
      continue;
    }

    // process working buffer in the first line
    if is_first_line {
      string_to_pairs(&input, &mut pairs);
      println!("Found first line: {} with pairs {:?}", input, pairs);
      is_first_line = false;
      continue;
    }

    let tokens : Vec<_>  = input.trim()
                              .split(" -> ")
                              .into_iter()
                              .collect();
    
    insertions.insert(String::from(tokens[0]), String::from(tokens[1]));
  }

  println!("Insertions: {:?}", insertions);

  for step in 1..41 {
    let mut new_pairs : HashMap<String, u64> = HashMap::new();
    for (key, value) in &mut pairs {
      match insertions.get(key) {
        Some(x) => {
          let prefix = key.chars().nth(0).unwrap();
          let prefix_pair = format!("{}{}", prefix, x);
          let suffix = key.chars().nth(1).unwrap();
          let suffix_pair = format!("{}{}", x, suffix);

          let pcnt = new_pairs.entry(String::from(prefix_pair)).or_insert(0);
          *pcnt += *value;
          let scnt = new_pairs.entry(String::from(suffix_pair)).or_insert(0);
          *scnt += *value;
        },
        None => {
          new_pairs.insert(String::from(key), *value);
        },
      }
    }

    pairs = new_pairs;
    print_ch_count(&mut pairs);
    println!("End of step {}", step);
    
  }

}