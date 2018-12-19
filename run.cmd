D:
cd .\
for /L %%B in (0,1,4) do start "Test1" runClient.cmd
runServer.cmd
TIMEOUT /T -1