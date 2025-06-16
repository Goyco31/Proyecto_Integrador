document.addEventListener('DOMContentLoaded', () => {
 const teamsData = [
  {
   id: 1,
   name: "Los Vengadores",
   members: ["IronMan", "CapAmerica", "Thor", "Hulk", "BlackWidow"]
  },
  {
   id: 2,
   name: "Guardianes de la Galaxia",
   members: ["StarLord", "Gamora", "Drax", "Rocket", "Groot"]
  },
  {
   id: 3,
   name: "X-Men United",
   members: ["Wolverine", "Cyclops", "JeanGrey", "Storm", "Beast"]
  },
  {
   id: 4,
   name: "Avengers Infinity",
   members: ["DoctorStrange", "SpiderMan", "CaptainMarvel", "Falcon", "WarMachine"]
  },
  {
   id: 5,
   name: "Wakanda Forever",
   members: ["BlackPanther", "Shuri", "Okoye", "M'Baku", "Nakia"]
  },
  {
   id: 6,
   name: "Asgardian Force",
   members: ["Loki", "Valkyrie", "Heimdall", "Sif", "Fandral"]
  },
  {
   id: 7,
   name: "Defenders League",
   members: ["Daredevil", "JessicaJones", "LukeCage", "IronFist", "Punisher"]
  },
  {
   id: 8,
   name: "Fantastic Four",
   members: ["MrFantastic", "InvisibleWoman", "HumanTorch", "TheThing", "SilverSurfer"]
  }
  // Puedes añadir más equipos aquí si lo necesitas
 ];

 const teamsGridLayout = document.querySelector('.teams-grid-layout');

 teamsData.forEach(team => {
  const teamBlock = document.createElement('div');
  teamBlock.classList.add('team-entry-block');

  const teamNumberContainer = document.createElement('div');
  teamNumberContainer.classList.add('team-number-container');
  const teamNumber = document.createElement('span');
  teamNumber.classList.add('team-number');
  teamNumber.textContent = team.id;
  teamNumberContainer.appendChild(teamNumber);
  teamBlock.appendChild(teamNumberContainer);

  const teamDetails = document.createElement('div');
  teamDetails.classList.add('team-details');

  const teamNameDisplay = document.createElement('h3');
  teamNameDisplay.classList.add('team-name-display-new');
  teamNameDisplay.textContent = team.name;
  teamDetails.appendChild(teamNameDisplay);

  const teamMembersList = document.createElement('div');
  teamMembersList.classList.add('team-members-list-new');

  team.members.forEach(member => {
   const memberItem = document.createElement('p');
   memberItem.classList.add('member-item-new');
   memberItem.textContent = member;
   teamMembersList.appendChild(memberItem);
  });

  teamDetails.appendChild(teamMembersList);
  teamBlock.appendChild(teamDetails);
  teamsGridLayout.appendChild(teamBlock);
 });
});