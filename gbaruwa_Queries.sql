--Retrieval SQL queries - used to find specific information:
--1. Find patients with the lowest weight amongst HIV patients.

SELECT P.PATIENT_ID, P.PATIENT_NAME, L.VALUE_1
FROM PATIENT P, ASSIGN_DISEASE A, DISEASE_CLASS D, OBSERVATION_TYPE O, OBSERVATIONS_LOG L
WHERE P.PATIENT_ID = A.PATIENT_ID AND A.CLASS_ID = D.CLASS_ID AND UPPER(D.CLASS_NAME) ='HIV'
AND L.PATIENT_ID = P.PATIENT_ID AND L.OBSERVATION_TYPE_ID =O.TYPE_ID AND UPPER(O.TYPE_NAME) ='WEIGHT'
AND L.VALUE_1 = (SELECT to_char(MIN(to_number(L1.VALUE_1))) FROM PATIENT P1, ASSIGN_DISEASE A1, DISEASE_CLASS D1, OBSERVATION_TYPE O1, OBSERVATIONS_LOG L1
WHERE P1.PATIENT_ID = A1.PATIENT_ID AND A1.CLASS_ID = D1.CLASS_ID AND UPPER(D1.CLASS_NAME) ='HIV'
AND L1.PATIENT_ID = P1.PATIENT_ID AND L1.OBSERVATION_TYPE_ID =O1.TYPE_ID AND UPPER(O1.TYPE_NAME) ='WEIGHT')
GROUP BY P.PATIENT_ID, P.PATIENT_NAME, L.VALUE_1;




--2. Of all Obesity and High Risk Patients, find patients with the highest blood pressure.
--FOR INDIVIDUAL SYSTOLIC AND DIASTOLIC

SELECT P.PATIENT_ID, P.PATIENT_NAME, L.VALUE_1, 'SYSTOLIC' AS BP_TYPE
FROM PATIENT P, ASSIGN_DISEASE A, DISEASE_CLASS D, OBSERVATION_TYPE O, OBSERVATIONS_LOG L
WHERE P.PATIENT_ID = A.PATIENT_ID AND A.CLASS_ID = D.CLASS_ID AND UPPER(D.CLASS_NAME) IN ('HIGH RISK PREGNANCY','OBESITY')
AND L.PATIENT_ID = P.PATIENT_ID AND L.OBSERVATION_TYPE_ID =O.TYPE_ID AND UPPER(O.TYPE_NAME) ='BLOOD PRESSURE'
AND SUBSTR(L.VALUE_1,1,INSTR(L.VALUE_1,'/')-1) =  
(
SELECT to_char(MAX(TO_NUMBER(SUBSTR(L1.VALUE_1,1,INSTR(L1.VALUE_1,'/')-1)))) 
FROM PATIENT P1, ASSIGN_DISEASE A1, DISEASE_CLASS D1, OBSERVATION_TYPE O1, OBSERVATIONS_LOG L1
WHERE P1.PATIENT_ID = A1.PATIENT_ID AND A1.CLASS_ID = D1.CLASS_ID AND UPPER(D1.CLASS_NAME) IN ('HIGH RISK PREGNANCY','OBESITY')
AND L1.PATIENT_ID = P1.PATIENT_ID AND L1.OBSERVATION_TYPE_ID =O1.TYPE_ID AND UPPER(O1.TYPE_NAME) ='BLOOD PRESSURE'
)
GROUP BY P.PATIENT_ID, P.PATIENT_NAME, L.VALUE_1
UNION
SELECT P.PATIENT_ID, P.PATIENT_NAME, L.VALUE_1 , 'DIASTOLIC' AS BP_TYPE
FROM PATIENT P, ASSIGN_DISEASE A, DISEASE_CLASS D, OBSERVATION_TYPE O, OBSERVATIONS_LOG L
WHERE P.PATIENT_ID = A.PATIENT_ID AND A.CLASS_ID = D.CLASS_ID AND UPPER(D.CLASS_NAME) IN ('HIGH RISK PREGNANCY','OBESITY')
AND L.PATIENT_ID = P.PATIENT_ID AND L.OBSERVATION_TYPE_ID =O.TYPE_ID AND UPPER(O.TYPE_NAME) ='BLOOD PRESSURE'
AND SUBSTR(L.VALUE_1,INSTR(L.VALUE_1,'/')+1) = 
(
SELECT to_char(MAX(TO_NUMBER(SUBSTR(L1.VALUE_1,INSTR(L1.VALUE_1,'/')+1)))) 
FROM PATIENT P1, ASSIGN_DISEASE A1, DISEASE_CLASS D1, OBSERVATION_TYPE O1, OBSERVATIONS_LOG L1
WHERE P1.PATIENT_ID = A1.PATIENT_ID AND A1.CLASS_ID = D1.CLASS_ID AND UPPER(D1.CLASS_NAME) IN ('HIGH RISK PREGNANCY','OBESITY')
AND L1.PATIENT_ID = P1.PATIENT_ID AND L1.OBSERVATION_TYPE_ID =O1.TYPE_ID AND UPPER(O1.TYPE_NAME) ='BLOOD PRESSURE'
)
GROUP BY P.PATIENT_ID, P.PATIENT_NAME, L.VALUE_1;



--FOR SYSTOLIC AND DIASTOLIC TOGETHER

SELECT P.PATIENT_ID, P.PATIENT_NAME, L.VALUE_1, 'SYSTOLIC' AS BP_TYPE
FROM PATIENT P, ASSIGN_DISEASE A, DISEASE_CLASS D, OBSERVATION_TYPE O, OBSERVATIONS_LOG L
WHERE P.PATIENT_ID = A.PATIENT_ID AND A.CLASS_ID = D.CLASS_ID AND UPPER(D.CLASS_NAME) IN ('HIGH RISK PREGNANCY','OBESITY')
AND L.PATIENT_ID = P.PATIENT_ID AND L.OBSERVATION_TYPE_ID =O.TYPE_ID AND UPPER(O.TYPE_NAME) ='BLOOD PRESSURE'
AND SUBSTR(L.VALUE_1,1,INSTR(L.VALUE_1,'/')-1) =  
(
SELECT to_char(MAX(TO_NUMBER(SUBSTR(L1.VALUE_1,1,INSTR(L1.VALUE_1,'/')-1)))) 
FROM PATIENT P1, ASSIGN_DISEASE A1, DISEASE_CLASS D1, OBSERVATION_TYPE O1, OBSERVATIONS_LOG L1
WHERE P1.PATIENT_ID = A1.PATIENT_ID AND A1.CLASS_ID = D1.CLASS_ID AND UPPER(D1.CLASS_NAME) IN ('HIGH RISK PREGNANCY','OBESITY')
AND L1.PATIENT_ID = P1.PATIENT_ID AND L1.OBSERVATION_TYPE_ID =O1.TYPE_ID AND UPPER(O1.TYPE_NAME) ='BLOOD PRESSURE'
)
AND SUBSTR(L.VALUE_1,INSTR(L.VALUE_1,'/')+1) = 
(
SELECT to_char(MAX(TO_NUMBER(SUBSTR(L1.VALUE_1,INSTR(L1.VALUE_1,'/')+1)))) 
FROM PATIENT P1, ASSIGN_DISEASE A1, DISEASE_CLASS D1, OBSERVATION_TYPE O1, OBSERVATIONS_LOG L1
WHERE P1.PATIENT_ID = A1.PATIENT_ID AND A1.CLASS_ID = D1.CLASS_ID AND UPPER(D1.CLASS_NAME) IN ('HIGH RISK PREGNANCY','OBESITY')
AND L1.PATIENT_ID = P1.PATIENT_ID AND L1.OBSERVATION_TYPE_ID =O1.TYPE_ID AND UPPER(O1.TYPE_NAME) ='BLOOD PRESSURE'
)
GROUP BY P.PATIENT_ID, P.PATIENT_NAME, L.VALUE_1;




--3. Find patients who have healthfriends with no outstanding alerts.

 SELECT A.PATIENT_ID FROM HEALTH_FRIEND A WHERE NOT EXISTS
(
 SELECT HEALTH_FRIEND_ID FROM HEALTH_FRIEND WHERE PATIENT_ID = A.PATIENT_ID
 INTERSECT
 SELECT OL.PATIENT_ID FROM OBSERVATIONS_LOG OL, MY_ALERTS MA, HEALTH_FRIEND HF
 WHERE OL.OBSERVATION_ID=MA.OBSERVATION_ID AND ALERT_STATUS = '1' AND OL.PATIENT_ID = HF.HEALTH_FRIEND_ID
 AND HF.PATIENT_ID = A.PATIENT_ID
)
GROUP BY A.PATIENT_ID;


--4. Find patients who live in same city as healthfriend.

SELECT HF.PATIENT_ID,HF.HEALTH_FRIEND_ID,PA.CITY FROM HEALTH_FRIEND HF, PATIENT_ADDRESS PA
WHERE HF.PATIENT_ID=PA.PATIENT_ID
AND lower(PA.CITY) =  
(SELECT lower(PA1.CITY) FROM HEALTH_FRIEND HF1, PATIENT_ADDRESS PA1 
WHERE HF1.HEALTH_FRIEND_ID=PA1.PATIENT_ID AND HF1.PATIENT_ID=PA.PATIENT_ID AND HF1.HEALTH_FRIEND_ID=HF.HEALTH_FRIEND_ID);
	
	
	
--5. For PatientX, list their healthfriends, ordered by date in which friendships were initiated.

SELECT HEALTH_FRIEND_ID,DATE_OF_INITIATION
FROM HEALTH_FRIEND
WHERE PATIENT_ID='ggeorge'
ORDER BY DATE_OF_INITIATION;


--Reporting queries - used to find more general information:
--1. For each patient, find the number of healthfriends made in the last month.

SELECT A.PATIENT_ID,SUM(A.COUNT1)
FROM
(SELECT P.PATIENT_ID,CASE WHEN EXTRACT(MONTH FROM DATE_OF_INITIATION) = EXTRACT(MONTH FROM CURRENT_DATE) - 1 THEN 1 ELSE 0 END AS COUNT1
FROM HEALTH_FRIEND HF RIGHT JOIN PATIENT P ON HF.PATIENT_ID = P.PATIENT_ID) A
GROUP BY A.PATIENT_ID;

--2. For each patient and each type of observation, show the number of such observations recorded by the patients.

select OL.PATIENT_ID, OT.TYPE_NAME, COUNT(*) 
FROM OBSERVATIONS_LOG OL, OBSERVATION_TYPE OT
WHERE OL.OBSERVATION_TYPE_ID = OT.TYPE_ID
GROUP BY OL.PATIENT_ID, OT.TYPE_NAME
ORDER BY OL.PATIENT_ID;



--3. For each patient, and each of their healthfriends, list the number of lingering alerts of the healthfriend.

SELECT HF.PATIENT_ID, HF.HEALTH_FRIEND_ID, COUNT(ALERT_ID) AS COUNT_LINGERING_ALERTS
FROM HEALTH_FRIEND HF, OBSERVATIONS_LOG OL, MY_ALERTS MA
WHERE HF.HEALTH_FRIEND_ID=OL.PATIENT_ID
AND OL.OBSERVATION_ID=MA.OBSERVATION_ID
AND MA.ALERT_STATUS = '1'
GROUP BY HF.PATIENT_ID, HF.HEALTH_FRIEND_ID
UNION
SELECT HF.PATIENT_ID, HF.HEALTH_FRIEND_ID, 0
FROM HEALTH_FRIEND HF, OBSERVATIONS_LOG OL, MY_ALERTS MA
WHERE HF.HEALTH_FRIEND_ID=OL.PATIENT_ID
AND OL.OBSERVATION_ID=MA.OBSERVATION_ID
AND MA.ALERT_STATUS = '0'
GROUP BY HF.PATIENT_ID, HF.HEALTH_FRIEND_ID;
















