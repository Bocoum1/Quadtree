using JuMP, HiGHS
function affectation(productivite::Matrix{Int64})
    #Creation du model
    model=Model(HiGHS.Optimizer)
    set_silent(model)

    @variable(model, x[1:6,1:6], binary=true)
    

    @objective(model,Max,sum(productivite[i,j]*x[i,j] for i in 1:6,j in 1:6))

    for i in 1:6
        @constraint(model, sum(x[i,j] for j in 1:6)==1)
    end
    return model
end
function tp2exo3()
    productivite=[
        13 24 31 19 40 29;
        18 25 30 15 43 22;
        20 20 27 25 34 33;
        23 26 28 18 37 30;
        28 33 34 17 38 20;
        19 36 25 27 45 24;
    ]
    # construction du Modele
    model=affectation(productivite)
    optimize!(model)
    statut=termination_status(model)
    if statut==MOI.OPTIMAL
        println("Probleme resolu à l'optimalité")
        println("Productivité totale : ",objective_value(model))
    end
end