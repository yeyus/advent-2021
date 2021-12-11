#!/usr/bin/env lua

require "lifo"
require "class"

function printtable(t)
	for i=1,#t do
		for j=1,#t[i] do
			if type(t[i][j]) == 'boolean' then
				local b = t[i][j] and 'X' or '_'
				io.write(b)
			else
				io.write(t[i][j])
			end

		end
		io.write("\n")
	end
end

Coordinate = class(function(co,i,j)
   co:set(i,j)
end)

function Coordinate.__eq(p1,p2) return p1._i == p2._i and p1._j == p2._j end
function Coordinate.__tostring(self) return string.format("[%d,%d]", self._i, self._j) end

function Coordinate.set(self,i,j)
	if type(i) == 'table' and getmetatable(i) == Coordinate then
     local po = x
     i = po.i
     j = po.j
  end
  self._i = i
  self._j = j
end

function Coordinate.i(self) return self._i end
function Coordinate.j(self) return self._j end

-- parse input into a table
matrix = {}
visited = {}

i = 1
for line in io.lines() do
	linesize = string.len(line)
	matrix[i] = {}
	visited[i] = {}
	for j=1,linesize do
		ch = string.sub(line, j, j)
		matrix[i][j] = tonumber(ch)
		visited[i][j] = false
	end
	i = i + 1
end

-- printtable(matrix)
-- printtable(visited)

local dir = { {-1, 0}, {0, -1}, {1, 0}, {0, 1} }
local start = Coordinate(0,0)
local result = 0 

local basins = {}

for i=1,#matrix do
	for j=1,#matrix[i] do
		-- print(string.format("Starting dfs on %d,%d", i,j))
		if visited[i][j] == true then goto cont3 end
		
		local basin = 0
		local start = Coordinate(i,j)
		local stack = Lifo.new()
		stack:push(start)

		while not stack:isEmpty() do

			-- print stack
			-- print(stack:debug())
			-- end debug

			local current = stack:pop()
			-- print(string.format("While starting on %s", current))

			local ci = current:i()
			local cj = current:j()
			local cv = matrix[ci][cj]
			if visited[ci][cj] then goto cont1 end
			
			visited[ci][cj] = true

			if cv == 9 then goto cont1 end
			
			-- print(string.format("basin value is %d + %d", basin, cv))
			basin = basin + 1 

			-- process neighbors
			local neigh = 0
			for d = 1, #dir do
				local ni = ci + dir[d][1]
				local nj = cj + dir[d][2]
				if ni < 1 or ni > #matrix then goto cont2 end
				if nj < 1 or nj > #matrix[1] then goto cont2 end
				local nv = matrix[ni][nj]
				-- if nv == 9 then goto cont2 end

				neigh = neigh + 1
				local n = Coordinate(ni,nj)
				stack:push(n)

				::cont2::
			end
			-- print(string.format("Coordinate %s has %d neighbors", current, neigh))

			-- printtable(visited)
			::cont1::
		end

		-- print(string.format("pushing basin value: %d", basin))
		basins[#basins + 1] = basin
		::cont3::
	end
end

table.sort(basins, function(a, b) return a > b end)
local result = 1 
for k,v in pairs(basins) do
	if k < 4 then
		result = result * v
	end
end
print(string.format("Result is %d", result))
