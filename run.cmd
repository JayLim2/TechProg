D:
cd .\
for /L %%B in (0,1,3) do start "Test1" runClient.cmd
runServer.cmd
TIMEOUT /T -1