#!/usr/bin/env lua

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

local Coordinate = { i = 0, j = 0 } 

function Coordinate:new(i, j)
	local o = {}
	setmetatable(o, { 
		__index = self,
		__eq = function(self, rhs)
			return self.i == rhs.i and self.j == rhs.j
		end,
		__tostring = function(self)
			return string.format("[%s,%s]", self.i, self.j)
		end
	})

	self.i = i or 0
	self.j = j or 0
	
	return o
end

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
local start = Coordinate:new(0,0)
local result = 0 

for i=1,#matrix do
	for j=1,#matrix[i] do
		if visited[i][j] == true then goto cont3 end
		
		local start = Coordinate:new(i,j)
		local stack = {}
		stack[1] = start

		while stack[1] do
			local current = table.remove(stack, 1)
			local ci = current.i
			local cj = current.j
			local cv = matrix[ci][cj]
			if visited[ci][cj] then goto cont1 end
			
			visited[ci][cj] = true
			-- process neighbors
			local neigh = 0
			for d = 1, #dir do
				local ni = ci + dir[d][1]
				local nj = cj + dir[d][2]
				if ni < 1 or ni > #matrix then goto cont2 end
				if nj < 1 or nj > #matrix[1] then goto cont2 end
				local nv = matrix[ni][nj]
				if nv > cv then goto cont2 end

				table.insert(stack, Coordinate:new(ni,nj))

				neigh = neigh + 1
				::cont2::
			end

			if neigh == 0 then
				print(string.format("%s,%s -> %d", ci, cj, cv))
				result = result + cv + 1
			end
			
			-- printtable(visited)
			::cont1::
		end
		::cont3::
	end
end

print(string.format("Result is %d", result))
