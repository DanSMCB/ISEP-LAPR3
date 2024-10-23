CREATE OR REPLACE PROCEDURE ReadHubsCSV(p_filename IN VARCHAR2) AS
  -- Declare a file handle for the .csv file
  file_handle UTL_FILE.FILE_TYPE;

  -- Declare a buffer to hold the data read from the file
  buffer VARCHAR2(32767);

  -- Declare variables to hold the column values
  id_loc VARCHAR2(5);
  latitude FLOAT;
  longitude FLOAT;
  tipo VARCHAR2(5);

  -- Declare a cursor to hold the parsed data
  CURSOR csv_data IS
    SELECT * FROM TABLE(
      parse_csv(utl_raw.cast_to_raw(buffer),',')
    );

  -- Declare a record to hold a row of data of the same type
  rec csv_data%ROWTYPE;

BEGIN
  
  -- Create Hub Table
  CREATE TABLE Input_Hub(
    idLoc VARCHAR2(5),
    latitude FLOAT,
    longitude FLOAT,
    tipo VARCHAR2(5)
  );

  -- Open the file for reading
  file_handle := UTL_FILE.FOPEN('DIRECTORY_NAME', p_filename, 'R');

  -- Read the file line by line until the end of the file is reached
  LOOP
    UTL_FILE.GET_LINE(file_handle, buffer);

    -- Parse the data in the buffer
    FOR rec IN csv_data LOOP
      id_loc := rec.col1;
      latitude := rec.col2;
      longitude := rec.col3;
      tipo := rec.col4;

      -- Insert the data into the table
      INSERT INTO my_table(id_loc, latitude, longitude, tipo)
      VALUES (id_loc, latitude, longitude, tipo);
    END LOOP;
  END LOOP;

  -- Close the file
  UTL_FILE.FCLOSE(file_handle);
END ReadHubsCSV;







CREATE OR REPLACE PROCEDURE UpdateHubs AS
  -- Declare a cursor to hold the rows from the source table
  CURSOR src_data IS
    SELECT col1, col2, col3, col4
    FROM src_table
    WHERE col4 NOT LIKE 'C%';

  -- Declare a record to hold a row of data of the same type
  rec src_data%ROWTYPE;
BEGIN
  -- Loop through the rows in the source table
  FOR rec IN src_data LOOP
    -- Insert the data into the destination table
    INSERT INTO dest_table(col1, col2, col3, col4)
    VALUES (rec.col1, rec.col2, rec.col3, rec.col4);
  END LOOP;
  --Drop Input_Hub table
  DROP TABLE Input_Hub;
END UpdateHubs;




CREATE OR REPLACE PROCEDURE UPDATE_CLIENT_LOC(p_id_cliente IN INT, p_id_loc IN VARCHAR2) AS
BEGIN
  -- Check if the specified location exists in the hub table
  IF NOT EXISTS (SELECT 1 FROM hub WHERE id_loc = p_id_loc) THEN
    RAISE_APPLICATION_ERROR(-20001, 'Invalid location: ' || p_id_loc);
  END IF;

  -- Update the client's location
  UPDATE cliente
  SET id_loc = p_id_loc
  WHERE id_cliente = p_id_cliente;

  -- Check if the update was successful
  IF SQL%ROWCOUNT = 0 THEN
    RAISE_APPLICATION_ERROR(-20002, 'No rows updated');
  END IF;
END UPDATE_CLIENT_LOC;




--Bloco de teste US215--
BEGIN
  ReadHubsCSV('FILENAME.csv')
  UpdateHubs;
  UPDATE_CLIENT_LOC(123, 'HUB1');
END;