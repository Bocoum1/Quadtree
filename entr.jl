 using JuMP, GLPK
 
 function solve_sans_commission()
    # Les données
    nbmois=5
    ca=200
    cs=6
    nbmax=500
    sinit=200
    d=[400,800,400,200,300]

    # Creation du model:

    model = Model(GLPK.Optimizer)

    # Les variables:
    @variable(model, x[1:nbmois]>=0, integer=true)
    @variable(model, s[1:nbmois]>=0, integer=true)

    #La fonction Objectif
    @objective(model, Min, sum(ca*x[i]+cs*s[i] for i in 1:nbmois))

    #Les contraintes

    #Pour le 1er mois
    @constraint(model, x[1]+sinit==d[1]+s[1])

    #Pour 2 jusqu'a nbmois
@constraint(model, [i in 2:nbmois], x[i] + s[i-1] == d[i] + s[i])
 #Pour la limite d'achats
@constraint(model, [i in 1:nbmois], x[i]<=nbmax )
#Resolution
optimize!(model)
#Affichage des resultats
status = termination_status(model)
if status == MOI.OPTIMAL
    println("Problème résolu à l'optimalité") 
    xsol = [value(x[i]) for i in 1:nbmois]
    ssol=[value(s[i]) for i in 1:nbmois]
    obj= objective_value(model)

    println("x = ",xsol)
    println("s = ",ssol) # affichage de la valeur optimale
    println("cout total = ",obj)
else 
    println(status," pas de solution optimale")
end
end