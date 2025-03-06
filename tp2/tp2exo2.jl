using JuMP, HiGHS

function model_5G(budget::Float64,sites::Vector{Char},cities::Vector{Int},cost::Dict{Char,Int64}, population::Dict{Int64,Int64}, coverage::Dict{Char,Vector{Int64}})
    #Creation du model
    model=Model(HiGHS.Optimizer)
    set_silent(model)

    @variable(model, x[sites], binary=true)
    @variable(model, y[cities], binary=true)

    @objective(model, Max,sum(population[j]*y[j] for j in cities))
    
    for j in cities
        sites_j=[i for i in sites if j in coverage[i]]
    @constraint(model, y[j]<=sum(x[i] for i in sites_j))
   end
   return model
end
function tp2exo2()
    #Declaration des données
    budget=10.0
    sites=['A', 'B', 'C', 'D', 'E', 'F', 'G']
    cost=Dict('A'=>3,'B'=>2,'C'=>3,'D'=>4,'E'=>4,'F'=>3,'G'=>2)
    cities = collect(1:15)
    population = Dict(
        1=>5, 2=>3, 3=>2, 4=>8, 5=>10,6=>3,7=>4,8=>6,9=>6,10=>3,
        11=>2,12=>7,13=>9,14=>3,15=>5
    )
coverage = Dict(
 'A'=>[1,2,3,4], 'B'=>[2,5,6],'C'=>[4,7,8],'D'=>[3,5,9],'E'=>[8,9,10,11],'F'=>[5,6,9,12,13],'G'=>[13,14,15]
)
#Le model complet avec les données
model::Model=model_5G(budget,sites,cities,cost,population,coverage)
#Resolution 
optimize!(model)
if termination_status(model)== MOI.OPTIMAL
    println("Probleme resolu à l'optimalité")
    println("population totale couverte = ",objective_value(model))
    varx=model[:x]
    varY=model[:y]
    x= value.(varx)
    y = value.(varY)
     println("sites implantés :")
     for s in sites
        if x[s] !=0
            println("site ",s, " : cout = ",cost[s])
        end
    end
    println("             ")
    for c in cities 
        if y[c] >0
            pop=+population[c]
            println("ville ", c, " pop = ", population[c])
        end
    println("pops : ",pop)
    end
 else
  println("Probleme non resolu à l'optimalité, statut=", status)
  end
end