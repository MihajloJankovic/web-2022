var re = document.getElementById("reson");
function uspesno(a)
{
    const FD = new FormData();
    FD.append("param",1);
    FD.append("id",a);
    const pera = Object.fromEntries(FD);
    const json1 = JSON.stringify(pera);
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "Medicine/adminorder", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log(xhr.responseText);
                alert("Successful");
                location.reload();
            } else {
                console.error(xhr.statusText);
            }
        }
    };
    xhr.send(json1);
}
function vraceno(a)
{
    const FD = new FormData();
    FD.append("param",2);
    FD.append("id",a);
    const pera = Object.fromEntries(FD);
    const json1 = JSON.stringify(pera);
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "Medicine/adminorder", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                console.log(xhr.responseText);
                alert("Successful");
                location.reload();

            } else {
                console.error(xhr.statusText);
            }
        }
    };
    xhr.send(json1);
}
function odbijeno(a)
{
    var b  = re.value.valueOf();
  if(b.trim().length > 5)
  {
      const FD = new FormData();
      FD.append("param",3);
      FD.append("id",a);
      FD.append("coment",b.trim());
      const pera = Object.fromEntries(FD);
      const json1 = JSON.stringify(pera);
      const xhr = new XMLHttpRequest();
      xhr.open("POST", "Medicine/adminorder", true);
      xhr.setRequestHeader("Content-Type", "application/json");
      xhr.onreadystatechange = function () {
          if (xhr.readyState === 4) {
              if (xhr.status === 200) {
                  console.log(xhr.responseText);
                  alert("Successful");
                  location.reload();
              } else {
                  console.error(xhr.statusText);
              }
          }
      };
      xhr.send(json1);
  }
  else
  {
    alert("State the reason for rejection !");
  }
}