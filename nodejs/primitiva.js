ms = new Array();
msString = '';
candidato = 0;
pares = 0;
impares = 0;
menores25 = 0;
mayoresI25 = 0;
repetidos = false;
tresPares = false;
tresImpares = false;
tresMenores25 = false;
tresMayoresI25 = false;
for (i = 0; i < 1000; i++) {
  for (j = 0; j < 6; j++) {
    do {
      candidato = (Math.round(Math.random() * 1000) % 49) + 1;
      repetidos = ms.includes(candidato);
      tresPares = candidato % 2 == 0 && pares == 3;
      tresImpares = candidato % 2 == 1 && impares == 3;
      tresMenores25 = candidato < 25 && menores25 == 3;
      tresMayoresI25 = candidato >= 25 && mayoresI25 == 3;
    } while (repetidos || tresPares || tresImpares || tresMenores25 || tresMayoresI25);
    candidato % 2 == 0 ? pares++ : impares++;
    candidato < 25 ? menores25++ : mayoresI25++;
    ms.push(candidato);
    msString += '-' + ms[j];
  }
  console.log(msString);
  ms = new Array();
  msString = '';
  pares = 0;
  impares = 0;
  menores25 = 0;
  mayoresI25 = 0;
}