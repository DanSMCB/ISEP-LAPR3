DECLARE order varchar2(10) := ascendente; 
BEGIN 
   IF order == "ascendente" THEN 
      select *
        from Parcela
        where idQuinta = 1;
        order by area (ASC); 
   END IF;

    IF order == "descendente" THEN
        select *
            from Parcela
            where idQuinta = 1;
            order by area (DESC);
    END IF;
END;