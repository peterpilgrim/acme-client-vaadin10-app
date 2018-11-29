# Keycloak Configuration

## Requirements

Information about configuration of Keycloak 


## Configuration

1. Download the Keycloak distribution version *4.4.0.Final*

 	Change to the directory `<KEYCLOAK_INSTALL_DIR>/standalone/configuration`

	Edit the file `standalone.xml`, change the HTTP port from 8080 to 9090 and also the SSL port from 8443 to 9443.
	So now the keycloak server starts on port 9090.

	Run the server in a separate terminal. Change to the directory `<KEYCLOAK_INSTALL_DIR>/bin`

	```sh
		$ ./standalone.sh
	```

2. Surf to the [keycloak server](http://localhost:9090/)

	 Create the new adminstration user with credentials: e.g. `admin`/`admin`

	 Log into the Keycloak server with new administration credentials.

3. Top left hand menu drop down, create a brand new realm with the parameters:

	```
	Name: demo
	Display name: demo
	Enabled: ON
	Endpoints: OpenID Endpoint Configuration
	```

	Leave the rest of parameters as defaults and then save the realm

4. Navigate to the `Roles` menu item on the left hand side menu. Create a new Role with these parameters:

	```
	Role Name: demo
	Description: user role
	Composite Roles: OFF
	```

	Leave the rest of parameters as defaults and then save the role.

5. Navigate to the `Users` menu item on the left hand side menu. Create a new User with these similar parameters:

	```
		Username: peter
		Email: peter.pilgrim@gmail.com
		First name: Peter
		Last name: Pilgrim
		User Enabled: ON
	```

	Leave the rest of parameters as defaults and then save the user.

	Go to the *Credentials* tab, under *Manage Password*
	Flick the *Temporary* toggle button to *OFF*
	Enter a new password and confirm the password.
	Click on the *Reset Password*

	Go to the *Role Mappings* tab, the select the `users` under *Available Users* and then click *Add Selected*. The username `peter` should be assigned to `users`.

	Go to the *Details* and click on *Save* to ensure the parameters have been applied.

6. Navigate to the `Clients` menu item on the left hand side menu. Create a new Client with these parameters:

		```
		Client ID: demoapp
		Client Protocal: openid-connect
		Root URL: http://localhost:9048/
		```

	The root URL is reference back to the Spring Boot application.
	
	**IMPORTANT** we also set the port number in the `application.properties` like so:
	
	```
    # Configure Vaadin Spring Boot application
    server.port=9048    
    ``` 
	
	Save the new client.

	In the *Settings* tab, verify the following parameters.

		```
		Enabled: ON
		Access type:  public
		Standard flow enabled: ON
		Implicit flow enabled: OFF
		Direct access grants enabled: ON
		Authorization Enabled: OFF
		Root URL: http://localhost:9048/
		Valid Redirect URIs: http://localhost:9048/*
		Base URL: ***BLANK***
		Admin URL: http://localhost:9048/
		Web Orgins: http://localhost:9048/
		```

	Navigate to the *Roles* tabs for *Clients*, we now add a new *Role* here.

		```
		Role Name: users
		Description: client role users
		Composite Roles: OFF
		```

	Save this role.

7. Navigate back to the *Manage* / *Users*.

	Select user `peter` and start editing this user again. Navigate to *Role Mappings* and then select the drop down *Client Roles*. Select `demoapp` from the down list, and assign the client role `user` to the assigned roles.

8. This should now work with the demonstration application.

## Manual Export and Import Database

I execute this command against Keycloak 4.4.0.Final.

```sh
	$ mkdir $HOME/tmp/keycloak-database
	$ standalone.sh -Dkeycloak.migration.action=export  -Dkeycloak.migration.provider=dir -Dkeycloak.migration.dir=$HOME/tmp/keycloak-database
```

The whole database is exported to the folder. The contents of the folder look like this:

```
	peterpilgrim@user-All-Series:Phoenix [509] ❯ ll ~/tmp/keycloak-database/
	total 184
	-rw-r--r-- 1 peterpilgrim devops 51583 Nov 28 10:27 demo-realm.json
	-rw-r--r-- 1 peterpilgrim devops  3317 Nov 28 10:27 demo-users-0.json
	-rw-r--r-- 1 peterpilgrim devops 51565 Nov 28 10:27 fundit-realm.json
	-rw-r--r-- 1 peterpilgrim devops  9750 Nov 28 10:27 fundit-users-0.json
	-rw-r--r-- 1 peterpilgrim devops 58437 Nov 28 10:27 master-realm.json
	-rw-r--r-- 1 peterpilgrim devops   921 Nov 28 10:27 master-users-0.json
```


It was then a simple matter to import into Keycloak 4.6.0.Final from the dump folder.


```sh
	$ standalone.sh -Dkeycloak.migration.action=import -Dkeycloak.migration.provider=dir  -Dkeycloak.migration.dir=$HOME/tmp/keycloak-database -Dkeycloak.migration.strategy=OVERWRITE_EXISTING
```

I would then remove the `master-realm.json` and `master-users-0.json` files.



## Investigation

Under Keycloak 4.6.0.Final. I still see the following warnings in the console

```
	10:33:22,882 WARN  [org.keycloak.events] (default task-1) type=REFRESH_TOKEN_ERROR, realmId=master, clientId=security-admin-console, userId=null, ipAddress=127.0.0.1, error=invalid_token, grant_type=refresh_token, client_auth_method=client-secret
	10:40:41,376 WARN  [org.keycloak.events] (default task-5) type=LOGOUT_ERROR, realmId=demo, clientId=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJqYTBjX18xMHJXZi1KTEpYSGNqNEdSNWViczRmQlpGS3NpSHItbDlud2F3In0.eyJqdGkiOiI1ZTdhYzQ4Zi1mYjkyLTRkZTYtYjcxNC01MTRlMTZiMmJiNDYiLCJleHAiOjE1NDM0MDE2MDksIm5iZiI6MCwiaWF0IjoxNTQzNDAxMzA5LCJpc3MiOiJodHRwOi8vMTI3Lj, userId=null, ipAddress=127.0.0.1, error=invalid_client_credentials
```

I am leaving this issue for now.

