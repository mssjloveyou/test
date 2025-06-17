#fj solo
docker run -p 9000:9000 --add-host mysql:172.18.0.7 --network myhadoop --ip 172.18.0.100 --name stock -d stock


近三年历史下降趋势

create view test as
SELECT
md.stock_id,
md.month,
si.close_price AS max_monthly_close_price
FROM
(
SELECT
stock_id,
month,
min(create_date) create_date
FROM
stock_info
WHERE
month >= '2023-01'
GROUP BY
stock_id,
month
) md
JOIN
stock_info si ON md.stock_id = si.stock_id AND md.create_date = si.create_date

select a.*,code.name from (
SELECT
stock_id,
CASE

        WHEN COUNT(CASE WHEN max_monthly_close_price > next_price THEN 1 END) > 20 THEN '1'
        ELSE '0' 
    END AS is_decreasing_trend
FROM (
SELECT
stock_id,
month,
max_monthly_close_price,

        LEAD(max_monthly_close_price) OVER (PARTITION BY stock_id ORDER BY month) AS next_price
    FROM
        test
) AS subquery
WHERE next_price IS NOT NULL
GROUP BY stock_id
) a left join stock_code code on a.stock_id=code.code where a.is_decreasing_trend=1
