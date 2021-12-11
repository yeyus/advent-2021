-- Mimics a LIFO stack, OOP style

Lifo = {}
Lifo.__index = Lifo

function Lifo.new()
  return setmetatable({}, Lifo)
end

-- Pushes a value at the head of the heap
function Lifo:push(value)
  self[#self + 1] = value 
end

-- Remove and return the value at the head of the heap
function Lifo:pop()
  assert(#self > 0, "Stack underflow")
  local output = self[#self]
  self[#self] = nil
  return output
end

-- Checks if the heap is empty
function Lifo:isEmpty() return #self == 0 end

function Lifo:debug() 
  for k,v in pairs(self) do print(string.format("[%d] -> %s,", k, v)) end
end
