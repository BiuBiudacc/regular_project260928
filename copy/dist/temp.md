## 学生信息创建接口


**接口地址**:`/api/student/create`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "studentName": "",
  "idCard": "",
  "studentGender": 0,
  "studentAge": 0,
  "studentPhone": "",
  "emergencyContactName": "",
  "emergencyContactPhone": "",
  "batchId": 0,
  "classId": 0
}
```


**请求参数**:


**请求参数**:


| 参数名称                          | 参数说明               | 请求类型 | 是否必须 | 数据类型               | schema                 |
| --------------------------------- | ---------------------- | -------- | -------- | ---------------------- | ---------------------- |
| studentCreateUpdateDTO            | StudentCreateUpdateDTO | body     | true     | StudentCreateUpdateDTO | StudentCreateUpdateDTO |
| &emsp;&emsp;studentName           | 学生名称               |          | true     | string                 |                        |
| &emsp;&emsp;idCard                | 身份证号码             |          | false    | string                 |                        |
| &emsp;&emsp;studentGender         | 学生性别               |          | false    | integer(int32)         |                        |
| &emsp;&emsp;studentAge            | 学生年龄               |          | false    | integer(int32)         |                        |
| &emsp;&emsp;studentPhone          | 学生联系方式           |          | false    | string                 |                        |
| &emsp;&emsp;emergencyContactName  | 学生紧急联系人         |          | false    | string                 |                        |
| &emsp;&emsp;emergencyContactPhone | 学生紧急联系人电话     |          | false    | string                 |                        |
| &emsp;&emsp;batchId               | 所属批次id             |          | true     | integer(int64)         |                        |
| &emsp;&emsp;classId               | 所属班级id             |          | false    | integer(int64)         |                        |


**响应状态**:


| 状态码 | 说明 | schema     |
| ------ | ---- | ---------- |
| 200    | OK   | ResultVoid |


**响应参数**:


| 参数名称 | 参数说明                | 类型           | schema         |
| -------- | ----------------------- | -------------- | -------------- |
| code     | 业务响应码，200代表成功 | integer(int32) | integer(int32) |
| msg      | 返回消息提示            | string         |                |
| data     | 数据主体                | object         |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {}
}
```

## 学生修改信息接口


**接口地址**:`/api/student/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "studentName": "",
  "idCard": "",
  "studentGender": 0,
  "studentAge": 0,
  "studentPhone": "",
  "emergencyContactName": "",
  "emergencyContactPhone": "",
  "batchId": 0,
  "classId": 0
}
```


**请求参数**:


**请求参数**:


| 参数名称                          | 参数说明               | 请求类型 | 是否必须 | 数据类型               | schema                 |
| --------------------------------- | ---------------------- | -------- | -------- | ---------------------- | ---------------------- |
| id                                | 学生                   | query    | true     | integer(int64)         |                        |
| studentCreateUpdateDTO            | StudentCreateUpdateDTO | body     | true     | StudentCreateUpdateDTO | StudentCreateUpdateDTO |
| &emsp;&emsp;studentName           | 学生名称               |          | true     | string                 |                        |
| &emsp;&emsp;idCard                | 身份证号码             |          | false    | string                 |                        |
| &emsp;&emsp;studentGender         | 学生性别               |          | false    | integer(int32)         |                        |
| &emsp;&emsp;studentAge            | 学生年龄               |          | false    | integer(int32)         |                        |
| &emsp;&emsp;studentPhone          | 学生联系方式           |          | false    | string                 |                        |
| &emsp;&emsp;emergencyContactName  | 学生紧急联系人         |          | false    | string                 |                        |
| &emsp;&emsp;emergencyContactPhone | 学生紧急联系人电话     |          | false    | string                 |                        |
| &emsp;&emsp;batchId               | 所属批次id             |          | true     | integer(int64)         |                        |
| &emsp;&emsp;classId               | 所属班级id             |          | false    | integer(int64)         |                        |


**响应状态**:


| 状态码 | 说明 | schema     |
| ------ | ---- | ---------- |
| 200    | OK   | ResultVoid |


**响应参数**:


| 参数名称 | 参数说明                | 类型           | schema         |
| -------- | ----------------------- | -------------- | -------------- |
| code     | 业务响应码，200代表成功 | integer(int32) | integer(int32) |
| msg      | 返回消息提示            | string         |                |
| data     | 数据主体                | object         |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {}
}
```

## 学生修改信息接口


**接口地址**:`/api/student/update`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "studentName": "",
  "idCard": "",
  "studentGender": 0,
  "studentAge": 0,
  "studentPhone": "",
  "emergencyContactName": "",
  "emergencyContactPhone": "",
  "batchId": 0,
  "classId": 0
}
```


**请求参数**:


**请求参数**:


| 参数名称                          | 参数说明               | 请求类型 | 是否必须 | 数据类型               | schema                 |
| --------------------------------- | ---------------------- | -------- | -------- | ---------------------- | ---------------------- |
| id                                | 学生                   | query    | true     | integer(int64)         |                        |
| studentCreateUpdateDTO            | StudentCreateUpdateDTO | body     | true     | StudentCreateUpdateDTO | StudentCreateUpdateDTO |
| &emsp;&emsp;studentName           | 学生名称               |          | true     | string                 |                        |
| &emsp;&emsp;idCard                | 身份证号码             |          | false    | string                 |                        |
| &emsp;&emsp;studentGender         | 学生性别               |          | false    | integer(int32)         |                        |
| &emsp;&emsp;studentAge            | 学生年龄               |          | false    | integer(int32)         |                        |
| &emsp;&emsp;studentPhone          | 学生联系方式           |          | false    | string                 |                        |
| &emsp;&emsp;emergencyContactName  | 学生紧急联系人         |          | false    | string                 |                        |
| &emsp;&emsp;emergencyContactPhone | 学生紧急联系人电话     |          | false    | string                 |                        |
| &emsp;&emsp;batchId               | 所属批次id             |          | true     | integer(int64)         |                        |
| &emsp;&emsp;classId               | 所属班级id             |          | false    | integer(int64)         |                        |


**响应状态**:


| 状态码 | 说明 | schema     |
| ------ | ---- | ---------- |
| 200    | OK   | ResultVoid |


**响应参数**:


| 参数名称 | 参数说明                | 类型           | schema         |
| -------- | ----------------------- | -------------- | -------------- |
| code     | 业务响应码，200代表成功 | integer(int32) | integer(int32) |
| msg      | 返回消息提示            | string         |                |
| data     | 数据主体                | object         |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {}
}
```

## 批次简易查询接口


**接口地址**:`/api/batch/listSimple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>仅返回必要信息</p>



**请求参数**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema                  |
| ------ | ---- | ----------------------- |
| 200    | OK   | ResultListBatchSimpleVO |


**响应参数**:


| 参数名称                   | 参数说明                | 类型              | schema         |
| -------------------------- | ----------------------- | ----------------- | -------------- |
| code                       | 业务响应码，200代表成功 | integer(int32)    | integer(int32) |
| msg                        | 返回消息提示            | string            |                |
| data                       | 数据主体                | array             | BatchSimpleVO  |
| &emsp;&emsp;batchId        | 批次id                  | integer(int64)    |                |
| &emsp;&emsp;batchName      | 批次名称                | string            |                |
| &emsp;&emsp;batchStartTime | 批次开始时间            | string(date-time) |                |
| &emsp;&emsp;batchEndTime   | 批次结束时间            | string(date-time) |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"batchId": 1,
			"batchName": "1",
			"batchStartTime": "",
			"batchEndTime": ""
		}
	]
}
```

## 班级信息简易查询接口


**接口地址**:`/api/class/listSimple`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>仅返回班级id,班级名称</p>



**请求参数**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema                  |
| ------ | ---- | ----------------------- |
| 200    | OK   | ResultListClassSimpleVO |


**响应参数**:


| 参数名称              | 参数说明                | 类型           | schema         |
| --------------------- | ----------------------- | -------------- | -------------- |
| code                  | 业务响应码，200代表成功 | integer(int32) | integer(int32) |
| msg                   | 返回消息提示            | string         |                |
| data                  | 数据主体                | array          | ClassSimpleVO  |
| &emsp;&emsp;id        | 班级id                  | integer(int64) |                |
| &emsp;&emsp;className | 班级名称                | string         |                |


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 1,
			"className": "1"
		}
	]
}
```