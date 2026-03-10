using JuMP, HiGHS
function model_entrepot(nbentr::Int64, nbcentr::Int64,coutfixe:: Vector{Int64},capacite::Vector{Int64},demande::Vector{Int64},CoutLivraison::Matrix{Float64},M::Float64)
    #Le model
    model=Model(HiGHS.Optimizer)
    set_silent(model)
    
    @variable(model,x[1:nbentr]>=0,binary=true)
    @variable(model, y[1:nbentr, 1:nbcentr]>=0 )


    @objective(model, Min,sum(coutfixe[i]*x[i] for i in 1:nbentr)+sum((CoutLivraison[i,j]/demande[j])*y[i,j] for i in 1:nbentr,j in 1:nbcentr if CoutLivraison[i,j]<M))
#Les contraintes:

    for j in 1:nbcentr
        @constraint(model,sum(y[i,j] for i in 1:nbentr)==demande[j])
    end

    for i in 1:nbentr
        @constraint(model,sum(y[i,j] for j in 1:nbcentr)<=capacite[i])
    end

    for i in 1:nbentr, j in 1:nbcentr
        if CoutLivraison[i,j]>=M
            @constraint(model,y[i,j]==0)
        end
    end
    return model
end

function tp2exo1()
    nbentr=12
    nbcentr=12
    coutfixe=[3500,2000,1000,4000,3000,900,4000,1000,2500,4000,1000,3500]
    capacite=[300,250,100,180,275,190,200,120,230,260,270,180]
    demande=[120,80,75,110,70,90,120,95,75,150,95,120,]
    M=Inf
    CoutLivraison=[
     100 80 50 50 60 100 120 90 60 70 65 110;
     120 90 60 70 65 110 140 110 80 80 75 130;
     140 110 80 80 75 130 160 125 100 100 80 150;
     160 125 100 100 80 150 190 150 130 M M M;
     190 150 130 M M M 200 180 150 M M M;
     200 180 150 M M M 100 80 50 50 60 100;
     100 80 50 50 60 100 120 90 60 70 65 110;
     120 90 60 70 65 110 140 110 80 80 75 130;
     140 110 80 80 75 130 160 125 100 100 80 150;
     160 125 100 100 80 150 190 150 130 M M M;
     190 150 130 M M M 200 180 150 M M M;
     200 180 150 M M M 100 80 50 50 60 100 ] 
    
    #le Modele complet avec les données
    model::Model=model_entrepot(nbentr,nbcentr,coutfixe,capacite,demande,CoutLivraison,M)
    #Resolution
    optimize!(model)
    if termination_status(model)== MOI.OPTIMAL
        println("Probleme resppolu à l'optimalité")
        #println("cout total  = ",objective_value(model))
        println("x")
        varx=model[:x]
        vary=model[:y]
        x=value.(varx)
        y=[[ value.(vary[i,j]) for j in 1:nbcentr] for i in 1:nbentr ]

        for i in 1:nbentr
            if x[i]>0
                println("Entrepot: ", i, " fixe :", coutfixe[i], "capacite: ", capacite[i])

            end
     end
     for i in 1:nbentr
        for j in 1:nbcentr
            if y[i][j]>M
                println("$(y[i][j]) tonnes de Entrpot $(i)  vers centrale $(j)")
            end
        end
    end
end
end