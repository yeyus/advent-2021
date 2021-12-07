#!/usr/bin/env ruby

fish = Array.new(9).fill(0)

numbers = STDIN.read.split(",").map { |s| s.to_i }

for n in numbers do
  fish[n] = fish[n] +  1
end

print "=== Initial state ===\n"
print "numbers: " + numbers.join(",") + "\n"
print "fish: " + fish.join(",") + "\n"

max_days = 256 
for day in 0 .. max_days - 1 do
  print "--- Day " + (day+1).to_s + "---"
  carry = 0;
  for f in 0 .. fish.length - 1 do
    if f == 0 then 
      carry = fish[f]
    end

    fish[f] = fish[f+1]
  end

  fish[6] += carry
  fish[8] = carry

  print "fish: " + fish.join(",") + " total: " + fish.inject(:+).to_s + "\n"
end

