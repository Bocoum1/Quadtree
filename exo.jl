function lire_map(fname::String)
    # Lire toutes les lignes du fichier
    lines = open(fname, "r") do file
        readlines(file)
    end
    n = length(lines)
    # Trouver la longueur maximale parmi toutes les lignes
    m = maximum(length.(lines))
    
    # Créer une matrice de type Char de dimensions n x m
    M = Matrix{Char}(undef, n, m)
    
    for i in 1:n
        line = lines[i]
        for j in 1:m
            if j <= length(line)
                M[i, j] = line[j]
            else
                # Si la ligne est plus courte, on complète avec un espace
                M[i, j] = '-'
            end
        end
    end
    return M
end

function cout_deplacement(cell::Char)
    if cell == 'S'
        return 5
    elseif cell == 'W'
        return 8
    else
        return 1
    end
end


function get_neighbors(M, pos::Tuple{Int,Int})
    l, c = pos
    voisins = Tuple{Int,Int}[]
    directions = [(0,1), (1,0), (0,-1), (-1,0)]  # les directions d'evolution
    for (dr, dc) in directions
        nl, nc = l + dr, c + dc
        if nl ≥ 1 && nl ≤ size(M,1) && nc ≥ 1 && nc ≤ size(M,2)
            
            # On vérifier que la case n'est pas un obstacle
            if M[nl,nc] != '@' 
                push!(voisins, (nl, nc))
            end
        end
    end
    return voisins;
end

function AlgoBfs(fname:String, D::Tuple{Int}, A::Tuple{Int})
    grid=lire_map(fname)
    queue=[D]
    visited=set([D])
    parent = Dict{Tuple{Int,Int},Tuple{Int,Int}}()
    #
    nb_evalueted=0
    while !isempty(queue) do 
        current=popfirst!(queue)
        nb_evalueted=+1
        if current==A
        path =reconstruire_path(parent,D,A)

    end
end