use std::io;
use std::io::BufRead;

#[derive(Copy,Clone,Debug)]
struct Point<T> {
    x: T,
    y: T
}

#[derive(Debug)]
enum Fold {
    Horizontal(u32),
    Vertical(u32),
}

fn fold(og : Point<u32>, fold: &Fold) -> Option<Point<u32>> {
    match fold {
        Fold::Horizontal(f) => {
            if og.y > *f {
                return Some(Point { x: og.x, y: *f - og.y % *f});
            } else if og.y == *f {
                return None
            } else {
                return Some(og);
            }
        },
        Fold::Vertical(f) => {
            if og.x > *f {
                return Some(Point { x: *f - og.x % *f, y: og.y });
            } else if og.x == *f {
                return None
            } else {
                return Some(og);
            }
        },
    }
}

fn print_grid(points: &Vec<Point<u32>>) {
    let maxx = points.iter().fold(0, |max, point| if point.x > max { point.x } else { max });
    let maxy = points.iter().fold(0, |max, point| if point.y > max { point.y } else { max });

    println!("Max X is {}", maxx);
    println!("Max Y is {}", maxy);

    let mut grid = vec![vec![false; maxx as usize] ; maxy as usize];

    points.iter().enumerate().for_each(|(i, arg)| {
        println!("Processing {} and {}", arg.x, arg.y);
        //grid[arg.x][arg.y] = true;
    });

    println!("The grid: {:?}", grid);
}

fn main() {
    let stdin = io::stdin();
    
    let mut points : Vec<Point<u32>> = Vec::new();
    let mut instructions : Vec<Fold> = Vec::new();
    //let p = Point { x: 0, y: 0};

    // input parsing
    let mut is_point_line = true;
    for line in stdin.lock().lines() {
        let input = line.expect("Failed to read line");

        // transition from point lines to folding lines
        if input.trim().len() == 0 {
            println!("Empty line");
            is_point_line = false;
            continue;
        }

        if is_point_line {
            //println!("Point line: {}", input);
            let coords : Vec<_>  = input.trim()
                .split(",")
                .into_iter()
                .filter_map(|i| i.parse::<u32>().ok())
                .collect();
            points.push(Point { x: coords[0], y: coords[1] });
        } else {
            let mut splitit = input.split(" ");
            let token = splitit.nth(2).unwrap();
            let fold_type = token.split("=").nth(0).unwrap();
            let fold_position = token.split("=").nth(1).unwrap();
            
            if fold_type == "x" {
                instructions.push(Fold::Vertical(fold_position.parse::<u32>().unwrap()));
            } else {
                instructions.push(Fold::Horizontal(fold_position.parse::<u32>().unwrap()));
            }
        }    

    }
    
    println!("List of points: {:?}", points);
    println!("List of instructions: {:?}", instructions);
    print_grid(&points);

    for inst in &instructions {
        let new_points : Vec<_> = points
                .iter()
                .filter_map(|point| {
                    let a =fold(*point, inst);
                    println!("Point {:?} transformed into {:?}", point, a);
                    return a;
                })
                .collect();
        println!("New points after applying {:?}: {:?}", inst, new_points);
    }
}
